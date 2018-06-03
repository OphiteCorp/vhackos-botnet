package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.MiningModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.MiningResponse;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiMaker;

import java.util.Map;

/**
 * Příkazy pro netcoin miner.
 *
 * @author mimic
 */
@Inject
public final class BotnetMinerCommand extends BaseCommand {

    @Autowired
    private MiningModule miningModule;

    protected BotnetMinerCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Zobrazí informace o mineru.
     */
    @Command(value = "miner", comment = "Gets information about ncMiner")
    private String getNetcoinMiner() {
        return execute("ncMiner", am -> {
            var data = miningModule.getMining();
            var fields = getFields(data, true);
            addMiningResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Sebere vytěžené netcoins z mineru.
     */
    @Command(value = "miner collect", comment = "Collect mined netcoins from the miner")
    private String collectMiner() {
        return execute("collect ncMiner", am -> {
            var data = miningModule.collect();
            var fields = getFields(data, true);
            addMiningResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Koupí další GPU do mineru.
     */
    @Command(value = "miner buy gpu", comment = "Buy next GPU into a miner")
    private String buyMinerGpu() {
        return execute("buy GPU for ncMiner", am -> {
            var data = miningModule.buyGpu();
            var fields = getFields(data, true);
            addMiningResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Spustí netcoin miner.
     */
    @Command(value = "miner start", comment = "Starts the netcoin miner")
    private String startMiner() {
        return execute("start ncMiner", am -> {
            var data = miningModule.start();
            var fields = getFields(data, true);
            addMiningResponseToAsciiMaker(am, fields);
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addMiningResponseToAsciiMaker(AsciiMaker am, Map<String, FieldData> fields) {
        put(am, fields.remove(MiningResponse.P_RUNNING));
        put(am, fields.remove(MiningResponse.P_NETCOINS));
        put(am, fields.remove(MiningResponse.P_GPU_COUNT));
        put(am, fields.remove(MiningResponse.P_WILL_EARN));
        put(am, fields.remove(MiningResponse.P_LEFT));
        putRemainings(am, fields);
    }
}
