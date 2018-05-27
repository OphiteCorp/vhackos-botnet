package cz.ophite.mimic.vhackos.botnet.api.opcode;

/**
 * Otevře všechny balíčky na serveru.
 *
 * @author mimic
 */
public final class ServerOpenAllPackagesOpcode extends ServerOpenPackageOpcode {

    @Override
    public String getOpcodeValue() {
        return "2000";
    }
}
