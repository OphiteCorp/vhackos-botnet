package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Získá profil crew.
 *
 * @author mimic
 */
public final class CrewProfileOpcode extends Opcode {

    private static final String PARAM_CREW_ID = "crew_id";

    /**
     * ID crew.
     */
    public void setCrewId(long crewId) {
        addParam(PARAM_CREW_ID, String.valueOf(crewId));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.CREW_PROFILE;
    }
}
