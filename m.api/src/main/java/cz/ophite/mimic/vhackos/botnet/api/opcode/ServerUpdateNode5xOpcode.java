package cz.ophite.mimic.vhackos.botnet.api.opcode;

/**
 * Vylepší node na serveru o 5.
 *
 * @author mimic
 */
public final class ServerUpdateNode5xOpcode extends ServerUpdateNode1xOpcode {

    @Override
    public String getOpcodeValue() {
        return "600";
    }
}
