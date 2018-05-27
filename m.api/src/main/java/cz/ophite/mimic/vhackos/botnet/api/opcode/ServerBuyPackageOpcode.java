package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Koupí 1 balíček za netcoins pro server.
 *
 * @author mimic
 */
public final class ServerBuyPackageOpcode extends Opcode {

    private static final String PARAM_COUNT = "count";

    /**
     * Počet balíčků kde jeden je za 40nc (max lze koupit 10 balíčků = 400nc).
     */
    public void setCount(int count) {
        addParam(PARAM_COUNT, String.valueOf(count));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.SERVER;
    }

    @Override
    public String getOpcodeValue() {
        return "555";
    }
}
