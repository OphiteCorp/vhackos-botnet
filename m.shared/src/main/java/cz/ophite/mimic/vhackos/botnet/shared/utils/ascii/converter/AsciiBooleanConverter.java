package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.IAsciiConverter;

/**
 * Převádí boolean na string.
 *
 * @author mimic
 */
@Inject
public final class AsciiBooleanConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        return SharedUtils.convertToBoolean(value);
    }
}
