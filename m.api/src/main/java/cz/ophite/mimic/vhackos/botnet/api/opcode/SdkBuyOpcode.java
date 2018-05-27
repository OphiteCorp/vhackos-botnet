package cz.ophite.mimic.vhackos.botnet.api.opcode;

/**
 * Koupí za netcoins další SDK pro exploit.
 *
 * @author mimic
 */
public final class SdkBuyOpcode extends SdkOpcode {

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
