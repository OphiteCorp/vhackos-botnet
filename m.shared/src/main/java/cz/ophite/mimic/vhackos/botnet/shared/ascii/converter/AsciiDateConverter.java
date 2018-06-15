package cz.ophite.mimic.vhackos.botnet.shared.ascii.converter;

import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Převede hodnotu času na formátovaný čas.
 *
 * @author mimic
 */
@Inject
public final class AsciiDateConverter implements IAsciiConverter {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss");

    @Override
    public Object convert(Object value) {
        var time = (long) value;
        var date = new Date(time * 1000);
        return SDF.format(date);
    }
}
