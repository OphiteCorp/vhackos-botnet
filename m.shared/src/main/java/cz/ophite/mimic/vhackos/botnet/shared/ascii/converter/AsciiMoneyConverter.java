package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

/**
 * Prevodník vstupní hodnoty na jinou pro výpis ASCII.
 *
 * @author mimic
 */
@Inject
public final class AsciiMoneyConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        return SharedUtils.toMoneyFormat(value);
    }
}
