package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii;

import de.vandermeer.asciitable.AT_Cell;
import de.vandermeer.asciitable.AT_ColumnWidthCalculator;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.skb.interfaces.document.TableRowType;
import de.vandermeer.skb.interfaces.transformers.Object_To_StrBuilder;
import de.vandermeer.skb.interfaces.transformers.String_To_ConditionalBreak;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import java.util.LinkedList;

/**
 * Strategie pro šířku sloupců pro ASCII.
 *
 * @author mimic
 */
public final class AsciiCWC implements AT_ColumnWidthCalculator {

    private int[] minWidths = new int[0];
    private int[] maxWidths = new int[0];

    public AsciiCWC add(final int minWidth, final int maxWidth) {
        this.minWidths = ArrayUtils.add(this.minWidths, minWidth);
        this.maxWidths = ArrayUtils.add(this.maxWidths, maxWidth);
        return this;
    }

    @Override
    public int[] calculateColumnWidths(LinkedList<AT_Row> rows, int colNumbers, int tableWidth) {
        Validate.notNull(rows);

        int[] resultWidths = new int[colNumbers];

        System.arraycopy(minWidths, 0, resultWidths, 0, minWidths.length > colNumbers ? colNumbers : minWidths.length);

        for (AT_Row row : rows) {
            if (row.getType() == TableRowType.CONTENT) {
                LinkedList<AT_Cell> cells = row.getCells();

                for (int i = 0; i < cells.size(); i++) {
                    if (cells.get(i).getContent() != null) {
                        String[] lines = String_To_ConditionalBreak
                                .convert(Object_To_StrBuilder.convert(cells.get(i).getContent()).toString());

                        for (String line : lines) {
                            int lineWidth = line.length() + cells.get(i).getContext().getPaddingLeft() + cells.get(i)
                                    .getContext().getPaddingRight();
                            if (lineWidth > resultWidths[i]) {
                                int maxWidth = (maxWidths.length > i) ? maxWidths[i] : 0;
                                if (maxWidth < 1 || lineWidth < maxWidth) {
                                    resultWidths[i] = lineWidth;
                                } else {
                                    resultWidths[i] = maxWidth;
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultWidths;
    }
}
