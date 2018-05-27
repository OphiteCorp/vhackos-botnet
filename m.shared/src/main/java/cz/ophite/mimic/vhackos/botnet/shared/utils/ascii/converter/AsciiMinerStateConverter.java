package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.dto.MinerState;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.IAsciiConverter;

/**
 * Převádí stav mineru.
 *
 * @author mimic
 */
@Inject
public final class AsciiMinerStateConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        return MinerState.getByCode((int) value).getAlias();
    }
}
