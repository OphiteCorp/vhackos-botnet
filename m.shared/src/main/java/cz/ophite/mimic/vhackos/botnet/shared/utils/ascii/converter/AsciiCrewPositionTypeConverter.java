package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.dto.CrewPositionType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.IAsciiConverter;

/**
 * Převede hodnotu pozice v crew na typ.
 *
 * @author mimic
 */
@Inject
public final class AsciiCrewPositionTypeConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        }
        return CrewPositionType.getbyPosition((int) value).getAlias();
    }
}
