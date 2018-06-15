package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;

/**
 * Převede čas v sekundach na čitelný formát.
 *
 * @author mimic
 */
@Inject
public final class AsciiElapsedSecondsTimeConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        if (value == null || ((long) value) < 0) {
            value = 0L;
        }
        long time = (long) value;
        return SharedUtils.toTimeFormat(time * 1000);
    }
}
