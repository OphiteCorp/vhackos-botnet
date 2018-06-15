package cz.ophite.mimic.vhackos.botnet.shared.ascii;

import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.AsciiTableException;
import de.vandermeer.asciithemes.TA_GridThemes;
import org.apache.commons.lang3.Validate;

import java.util.Collection;

/**
 * Vlastní instance ASCII makeru, protože jr potřeba použít vlastní renderer a upravit některé vstupní znaky.
 *
 * @author mimic
 */
public final class AsciiMaker extends AsciiTable {

    static final char SPACE_CHAR = (char) 160; // (&nbsp; => 160)

    public AsciiMaker(boolean insideTheme) {
        super();
        renderer = AsciiRenderer.create();
        renderer.setCWC(new AsciiCWC());

        if (insideTheme) {
            setInsideTheme();
        }
    }

    public AsciiMaker() {
        this(false);
    }

    public void setInsideTheme() {
        getContext().setGridTheme(TA_GridThemes.INSIDE);
    }

    public void setTopTheme() {
        getContext().setGridTheme(TA_GridThemes.TOP);
    }

    public AT_Row add(Collection<?> columns) throws NullPointerException, AsciiTableException {
        Validate.notNull(columns);
        return add(columns.toArray());
    }

    public AT_Row add(Object... columns) throws NullPointerException, AsciiTableException {
        Object[] out;

        if (columns != null && columns.length > 0) {
            out = new Object[columns.length];

            for (var i = 0; i < columns.length; i++) {
                var o = columns[i];

                if (o != null && o instanceof String) {
                    out[i] = ((String) o).replace((char) 32, SPACE_CHAR);
                } else {
                    out[i] = o;
                }
            }
        } else {
            out = columns;
        }
        return addRow(out);
    }
}
