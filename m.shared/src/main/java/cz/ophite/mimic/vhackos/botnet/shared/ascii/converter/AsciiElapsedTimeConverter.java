package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;

/**
 * Převede čas v milisekundách na čitelný formát.
 *
 * @author mimic
 */
@Inject
public final class AsciiElapsedTimeConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        }
        long time = (long) value;
        return SharedUtils.toTimeFormat(time);
    }
}
