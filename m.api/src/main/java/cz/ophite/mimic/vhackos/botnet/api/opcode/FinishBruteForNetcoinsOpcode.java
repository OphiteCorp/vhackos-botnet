package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Dokončí bruteforce za netcoins.
 *
 * @author mimic
 */
public final class FinishBruteForNetcoinsOpcode extends Opcode {

    private static final String PARAM_UPDATE_ID = "updateid";

    /**
     * Bruteforce ID.
     */
    public void setBruteforceId(int bruteId) {
        addParam(PARAM_UPDATE_ID, String.valueOf(bruteId));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.TASKS;
    }

    @Override
    public String getOpcodeValue() {
        return "20000";
    }
}
