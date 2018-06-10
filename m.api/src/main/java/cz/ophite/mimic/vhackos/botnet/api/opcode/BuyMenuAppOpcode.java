package cz.ophite.mimic.vhackos.botnet.api.opcode;

/**
 * Koupí menu aplikaci v obchodu.
 *
 * @author mimic
 */
public final class BuyMenuAppOpcode extends BuyAppOpcode {

    @Override
    public String getOpcodeValue() {
        return "200";
    }
}
