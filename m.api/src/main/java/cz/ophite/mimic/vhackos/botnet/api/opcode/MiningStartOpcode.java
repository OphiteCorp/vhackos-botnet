package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Spustí netcoin miner.
 *
 * @author mimic
 */
public final class MiningStartOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.MINING;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
