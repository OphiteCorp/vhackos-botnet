package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Vybere netcoins z mineru.
 *
 * @author mimic
 */
public final class MiningCollectOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.MINING;
    }

    @Override
    public String getOpcodeValue() {
        return "200";
    }
}
