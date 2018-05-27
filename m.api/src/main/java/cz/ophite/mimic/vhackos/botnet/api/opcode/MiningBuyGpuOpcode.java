package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Zakoupí další GPU do mineru.
 *
 * @author mimic
 */
public final class MiningBuyGpuOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.MINING;
    }

    @Override
    public String getOpcodeValue() {
        return "8888";
    }
}
