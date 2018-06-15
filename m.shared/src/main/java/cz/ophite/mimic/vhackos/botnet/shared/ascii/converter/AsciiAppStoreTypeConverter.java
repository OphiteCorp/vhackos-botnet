package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Získá název aplikace podle ID.
 *
 * @author mimic
 */
@Inject
public final class AsciiAppStoreTypeConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        var type = AppStoreType.getById((int) value);
        return type.getAlias();
    }
}
