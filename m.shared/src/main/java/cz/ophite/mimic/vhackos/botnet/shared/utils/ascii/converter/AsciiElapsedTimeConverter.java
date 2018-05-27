package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.IAsciiConverter;

/**
 * Převede čas v milisekundách na čitelný formát.
 *
 * @author mimic
 */
@Inject
public final class AsciiElapsedTimeConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        long time = (long) value;
        return SharedUtils.toTimeFormat(time);
    }
}
