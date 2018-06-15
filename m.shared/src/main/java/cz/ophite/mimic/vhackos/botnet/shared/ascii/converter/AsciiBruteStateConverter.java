package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;
import cz.ophite.mimic.vhackos.botnet.shared.dto.BruteState;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Převede hodnotu stavu bruteforce na typ.
 *
 * @author mimic
 */
@Inject
public final class AsciiBruteStateConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        return BruteState.getbyState((int) value).getAlias();
    }
}
