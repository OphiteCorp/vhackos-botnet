package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Zakoupí novou IP adresu (za 1000 netcoins).
 *
 * @author mimic
 */
public final class BuyNewIpOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.BUY;
    }

    @Override
    public String getOpcodeValue() {
        return "200";
    }
}
