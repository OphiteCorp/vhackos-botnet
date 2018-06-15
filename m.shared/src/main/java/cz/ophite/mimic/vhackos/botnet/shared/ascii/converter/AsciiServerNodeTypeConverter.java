package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.dto.ServerNodeType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;

/**
 * Převede hodnotu typu nodu na serveru na typ.
 *
 * @author mimic
 */
@Inject
public final class AsciiServerNodeTypeConverter implements IAsciiConverter {

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        }
        return ServerNodeType.getByCode((int) value).getAlias();
    }
}
