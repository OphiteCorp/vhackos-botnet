package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Vybere bankovní účet banky.
 *
 * @author mimic
 */
public final class RemoteBankWithdrawOpcode extends Opcode {

    private static final String PARAM_TARGET = "target";
    private static final String PARAM_AMOUNT = "amount";

    /**
     * Cílová IP.
     */
    public void setTargetIp(String ip) {
        addParam(PARAM_TARGET, ip);
    }

    /**
     * Množství peněz.
     */
    public void setAmount(long amount) {
        addParam(PARAM_AMOUNT, String.valueOf(amount));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.REMOTE_BANKING;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
