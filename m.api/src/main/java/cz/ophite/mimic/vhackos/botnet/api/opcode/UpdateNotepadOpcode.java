package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Nastaví data do poznámkového bloku.
 *
 * @author mimic
 */
public final class UpdateNotepadOpcode extends Opcode {

    private static final String PARAM_NOTEPAD = "notepad";

    /**
     * Nový obsah v poznámkovém bloku.
     */
    public void setNotepad(String notepad) {
        addParam(PARAM_NOTEPAD, notepad);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.NOTEPAD;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
