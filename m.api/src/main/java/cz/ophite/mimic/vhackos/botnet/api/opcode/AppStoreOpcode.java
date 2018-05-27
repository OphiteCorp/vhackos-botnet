package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Získá informace o aplikacích z obchodu.
 *
 * @author mimic
 */
public final class AppStoreOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.STORE;
    }
}
