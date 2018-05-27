package cz.ophite.mimic.vhackos.botnet.api.opcode;

/**
 * Dokončí misi.
 *
 * @author mimic
 */
public final class MissionsClaimOpcode extends MissionsOpcode {

    private static final String PARAM_DAILY_ID = "dailyid";

    /**
     * ID mise. Rozmezí 0-3.
     */
    public void setDailyId(int dailyId) {
        addParam(PARAM_DAILY_ID, String.valueOf(dailyId));
    }

    @Override
    public String getOpcodeValue() {
        return "200";
    }
}
