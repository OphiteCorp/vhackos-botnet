package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.dto.MissionFinishedType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;

/**
 * Získá název stavu mise podle kódu.
 *
 * @author mimic
 */
@Inject
public final class AsciiMissionFinishedTypeConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        return MissionFinishedType.getByCode((int) value).getAlias();
    }
}
