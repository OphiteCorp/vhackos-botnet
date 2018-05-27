package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Získá informace u leaderboards.
 *
 * @author mimic
 */
public final class LeaderboardsOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.RANKING;
    }
}
