package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidAccessTokenException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidRequestException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.opcode.NotepadOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.UpdateNotepadOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spravuje poznámkový blok.
 *
 * @author mimic
 */
@Inject
public final class NotepadModule extends Module {

    private static final String OK_CODE_NOTEPAD_SET = "2";
    private static final String ERR_CODE_NOTEPAD_INVALID_TOKEN = "1";
    private static final String ERR_CODE_SET_NOTEPAD_INVALID_COMMAND = "1";

    protected NotepadModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získání obsahu poznámkového bloku.
     */
    public synchronized List<String> getNotepad() {
        var opcode = new NotepadOpcode();

        try {
            var response = sendRequest(opcode);
            var notepad = response.get("notepad").toString().split("\n");
            return new ArrayList<>(Arrays.asList(notepad));

        } catch (BotnetException e) {
            if (ERR_CODE_NOTEPAD_INVALID_TOKEN.equals(e.getResultCode())) {
                throw new InvalidAccessTokenException(e
                        .getResultCode(), "Your access token is no longer valid, try signing in again");
            }
            throw e;
        }
    }

    /**
     * Nastaví data do poznámkového bloku.
     */
    public synchronized void setNotepad(List<String> lines) {
        var linesStr = StringUtils.join(lines, "\\n");
        linesStr = linesStr.replaceAll("\\\\n", "\n");

        var opcode = new UpdateNotepadOpcode();
        opcode.setNotepad(linesStr);

        try {
            sendRequest(opcode);

        } catch (BotnetException e) {
            if (OK_CODE_NOTEPAD_SET.equals(e.getResultCode())) {
                // nic, poznámkový blok byl uložen
                return;

            } else if (ERR_CODE_SET_NOTEPAD_INVALID_COMMAND.equals(e.getResultCode())) {
                throw new InvalidRequestException(e
                        .getResultCode(), "The command contains invalid values in the parameter");
            }
            throw e;
        }
    }
}
