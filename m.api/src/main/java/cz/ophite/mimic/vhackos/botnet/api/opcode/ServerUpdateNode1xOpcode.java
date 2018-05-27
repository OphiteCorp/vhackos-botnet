package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;
import cz.ophite.mimic.vhackos.botnet.shared.dto.ServerNodeType;

/**
 * Vylepší node na serveru o 1.
 *
 * @author mimic
 */
public class ServerUpdateNode1xOpcode extends Opcode {

    private static final String PARAM_NODE_NUMBER = "node_number";
    private static final String PARAM_NODE_TYPE = "node_type";

    /**
     * Typ nodu.
     */
    public void setNodeType(ServerNodeType nodeType) {
        addParam(PARAM_NODE_TYPE, String.valueOf(nodeType.getCode()));
    }

    /**
     * Pořadí nodu. Pro server je to vždy 1. Pro ostatní 1 až 3.
     */
    public void setNodeNumber(int number) {
        addParam(PARAM_NODE_NUMBER, String.valueOf(number));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.SERVER;
    }

    @Override
    public String getOpcodeValue() {
        return "500";
    }
}
