package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.BruteforceAlreadyRunningException;
import cz.ophite.mimic.vhackos.botnet.api.exception.ExploitException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidAccessTokenException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.BankResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.RemoteBankResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.*;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

import java.util.Collections;
import java.util.Map;

/**
 * Operace nad bankami.
 *
 * @author mimic
 */
@Inject
public final class BankModule extends Module {

    private static final String ERR_CODE_BRUTEFORCE_IS_ALREADY_EXISTS = "2";

    protected BankModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá banku hráče.
     */
    public synchronized BankResponse getBank() {
        var opcode = new BankOpcode();
        var response = sendRequest(opcode);
        var result = getResultValue(response);

        // nestačí pouze kontrola resultu na null, protože pří správné odpovědi též vrací null, ale celkový počet peněz
        // musí být vždy
        if (result == null && response.getOrDefault(BankResponse.KEY_TOTAL, null) == null) {
            throw new InvalidAccessTokenException(null, "Your access token is no longer valid, try signing in again");
        }
        return createBankResponse(response);
    }

    /**
     * Zahájí prolamování banky.
     */
    public synchronized void bruteforce(String ip) {
        var opcode = new BruteforceRemoteBankOpcode();
        opcode.setTargetIp(ip);

        try {
            sendRequest(opcode);

        } catch (BotnetException e) {
            if (ERR_CODE_BRUTEFORCE_IS_ALREADY_EXISTS.equals(e.getResultCode())) {
                throw new BruteforceAlreadyRunningException(e
                        .getResultCode(), "Target IP '" + ip + "' already exists in the list");
            }
            throw e;
        }
    }

    /**
     * Získá informace o cílové bance.
     */
    public synchronized RemoteBankResponse getRemoteBank(String ip) {
        var opcode = new RemoteBankOpcode();
        opcode.setTargetIp(ip);

        var response = sendRequest(opcode);
        var result = getResultValue(response);

        if (result == null) {
            throw new ExploitException(result, "IP '" + ip + "' does not exist or you do not have permissions");
        }
        return createRemoteBankResponse(response);
    }

    /**
     * Vybere banku z cílové IP.
     */
    public synchronized RemoteBankResponse withdraw(String ip, long amount) {
        var opcode = new RemoteBankWithdrawOpcode();
        opcode.setTargetIp(ip);
        opcode.setAmount(amount);

        var response = sendRequest(opcode);
        return createRemoteBankResponse(response);
    }

    /**
     * Skryje IP pomocí malware.
     */
    public synchronized RemoteBankResponse useMalware(String ip) {
        var opcode = new UseMalwareOpcode();
        opcode.setTargetIp(ip);

        var response = sendRequest(opcode);
        return createRemoteBankResponse(response);
    }

    private BankResponse createBankResponse(Map<String, Object> response) {
        var dto = new BankResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), BankResponse.class);
        ModuleHelper.setField(response, dto, BankResponse.P_FILLED);
        ModuleHelper.setField(response, dto, BankResponse.P_MONEY);
        ModuleHelper.setField(response, dto, BankResponse.P_SAVINGS);
        ModuleHelper.setField(response, dto, BankResponse.P_USER_NAME);
        ModuleHelper.setField(response, dto, BankResponse.P_TOTAL);
        ModuleHelper.setField(response, dto, BankResponse.P_MAX_SAVINGS);
        ModuleHelper.setField(response, dto, BankResponse.P_TRANSACTIONS_COUNT);

        if (!ModuleHelper.setField(response, dto, BankResponse.P_TRANSACTIONS, (f, data) -> ModuleHelper
                .convertToTransactionData(data))) {
            dto.setTransactions(Collections.emptyList());
        }
        return dto;
    }

    private RemoteBankResponse createRemoteBankResponse(Map<String, Object> response) {
        var dto = new RemoteBankResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), RemoteBankResponse.class);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_WITHDRAW);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_OPEN);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_IP_REMOVED);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_TARGET_ID);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_REMOTE_USER_NAME);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_USER_NAME);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_REMOTE_PASSWORD);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_REMOTE_MONEY);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_MONEY);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_SAVINGS);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_USER_MALWARE_KITS);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_GOT_BLT);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_AATT);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_NEXT_P);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_TOTAL);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_MAX_SAVINGS);
        ModuleHelper.setField(response, dto, RemoteBankResponse.P_TRANSACTIONS_COUNT);

        if (!ModuleHelper.setField(response, dto, RemoteBankResponse.P_TRANSACTIONS, (f, data) -> ModuleHelper
                .convertToTransactionData(data))) {
            dto.setTransactions(Collections.emptyList());
        }
        return dto;
    }
}
