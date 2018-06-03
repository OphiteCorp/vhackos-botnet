package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.dto.ConnectionData;
import cz.ophite.mimic.vhackos.botnet.api.module.BankModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.BankResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.RemoteBankResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.BankTransactionData;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiMaker;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Příkazy kolem banky.
 *
 * @author mimic
 */
@Inject
public final class BotnetBankCommand extends BaseCommand {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private ConnectionData connection;

    @Autowired
    private BankModule bankModule;

    protected BotnetBankCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Získá podorbné informace o vzdálené bance.
     */
    @Command(value = "bank", comment = "Gets information about your own bank")
    private String getBank() {
        return execute("bank", am -> {
            var data = bankModule.getBank();
            var fields = getFields(data, true);
            addBankResponseToAsciiMaker(am, fields, connection.getUid());
        });
    }

    /**
     * Začne prolamovat banku.
     */
    @Command(value = "bank brute", comment = "It starts breaking the bank")
    private String bruteforce(@CommandParam("ip") String ip) {
        return execute("bank bruteforce -> " + ip, am -> {
            bankModule.bruteforce(ip);
            put(am, "Result", "Bruteforce was started");
        });
    }

    /**
     * Získá informace o vzdálené bance.
     */
    @Command(value = "bank remote", comment = "Gets information about the remote bank")
    private String getRemoteBank(@CommandParam("ip") String ip) {
        return execute("bank remote -> " + ip, am -> {
            var data = bankModule.getRemoteBank(ip);
            var fields = getFields(data, true);
            addRemoteBankResponseToAsciiMaker(am, fields, data.getTargetId());
        });
    }

    /**
     * Použije malware na cílovou banku pro skrytí IP.
     */
    @Command(value = "bank apply malware", comment = "Use malware to hide IP")
    private String useMalware(@CommandParam("ip") String ip) {
        return execute("bank apply malware -> " + ip, am -> {
            var data = bankModule.useMalware(ip);
            var fields = getFields(data, true);
            addRemoteBankResponseToAsciiMaker(am, fields, data.getTargetId());
        });
    }

    /**
     * Získá podorbné informace o vzdálené bance.
     */
    @Command(value = "bank withdraw", comment = "Steals money from the target bank")
    private String withdraw(@CommandParam("ip") String ip, @CommandParam("amount") final String amountPattern) {
        return execute("bank withdraw -> " + ip, am -> {
            var pattern = amountPattern.toLowerCase();
            boolean milions;

            if (milions = pattern.endsWith("m")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            long amount = Long.parseLong(pattern);
            if (milions) {
                amount *= 1e6;
            }
            var data = bankModule.withdraw(ip, amount);
            var fields = getFields(data, true);
            addRemoteBankResponseToAsciiMaker(am, fields, data.getTargetId());
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addBankResponseToAsciiMaker(AsciiMaker am, Map<String, FieldData> fields, int targetId) {
        put(am, fields.remove(BankResponse.P_USER_NAME));

        var savings = String.format("%s / %s", StringUtils
                .leftPad(fields.remove(BankResponse.P_SAVINGS).value.toString(), 11), StringUtils
                .rightPad(fields.remove(BankResponse.P_MAX_SAVINGS).value.toString(), 11));
        put(am, "Savings", savings);

        put(am, fields.remove(BankResponse.P_MONEY));
        put(am, fields.remove(BankResponse.P_TOTAL));
        put(am, fields.remove(BankResponse.P_TRANSACTIONS_COUNT));
        convertBankTransactions(am, fields.remove(BankResponse.P_TRANSACTIONS), targetId);

        putRemainings(am, fields);
    }

    private void addRemoteBankResponseToAsciiMaker(AsciiMaker am, Map<String, FieldData> fields, int targetId) {
        put(am, fields.remove(RemoteBankResponse.P_USER_NAME));
        put(am, fields.remove(RemoteBankResponse.P_TARGET_ID));

        var savings = String.format("%s / %s", StringUtils
                .leftPad(fields.remove(RemoteBankResponse.P_SAVINGS).value.toString(), 11), StringUtils
                .rightPad(fields.remove(RemoteBankResponse.P_MAX_SAVINGS).value.toString(), 11));
        put(am, "Savings", savings);

        put(am, fields.remove(RemoteBankResponse.P_MONEY));
        put(am, fields.remove(RemoteBankResponse.P_TOTAL));
        put(am, fields.remove(RemoteBankResponse.P_USER_MALWARE_KITS));
        put(am, fields.remove(RemoteBankResponse.P_TRANSACTIONS_COUNT));
        convertBankTransactions(am, fields.remove(BankResponse.P_TRANSACTIONS), targetId);

        putRemainings(am, fields);
    }

    private void convertBankTransactions(AsciiMaker am, FieldData data, Integer targetId) {
        var transactions = (List<BankTransactionData>) data.value;
        var name = data.name;

        for (var i = 0; i < transactions.size(); i++) {
            var trans = transactions.get(i);

            var fields = getFields(trans, true);
            var time = fields.get(BankTransactionData.P_TIME).value;
            var plus = targetId.equals(trans.getToId());

            // založí transaci do databáze a pokusí se odhalit skryté IP (upravuje referenci)
            var state = databaseService.addTransaction(trans);
            var revealFrom = state.getKey();
            var revealTo = state.getValue();

            var str = String
                    .format("%s [%s -> %s] - [%s -> %s] %s %s", StringUtils.rightPad(time.toString(), 20), StringUtils
                            .leftPad(trans.getFromId().toString(), 7), StringUtils
                            .rightPad(trans.getToId().toString(), 7), StringUtils
                            .leftPad(trans.getFromIp() + (revealFrom ? "*" : ""), 16), StringUtils
                            .rightPad(trans.getToIp() + (revealTo ? "*" : ""), 16), (plus ? "+" : "-"), StringUtils
                            .leftPad(SharedUtils.toMoneyFormat(trans.getAmount()), 17));

            put(am, (i == 0) ? name : "", str);
        }
        if (transactions.isEmpty()) {
            put(am, name, "<none>");
        }
    }
}
