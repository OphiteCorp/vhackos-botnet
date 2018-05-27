package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.dto.BruteState;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.IAsciiConverter;

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
