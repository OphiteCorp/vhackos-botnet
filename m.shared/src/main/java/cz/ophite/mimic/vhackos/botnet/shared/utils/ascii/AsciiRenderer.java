package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii;

import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.TA_GridConfig;
import de.vandermeer.skb.interfaces.render.DoesRenderToWidth;
import de.vandermeer.skb.interfaces.render.RendersToClusterWidth;
import de.vandermeer.skb.interfaces.strategies.collections.list.ArrayListStrategy;
import de.vandermeer.skb.interfaces.transformers.ClusterElementTransformer;
import de.vandermeer.skb.interfaces.transformers.Object_To_StrBuilder;
import de.vandermeer.skb.interfaces.transformers.StrBuilder_To_String;
import de.vandermeer.skb.interfaces.transformers.arrays2d.Array2D_To_FlipArray;
import de.vandermeer.skb.interfaces.transformers.arrays2d.Array2D_To_NormalizedArray;
import de.vandermeer.skb.interfaces.transformers.textformat.TextFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Vlastní ASCII renderer, protože není jiná možnost, aby šlo použít vlastní formáter.
 *
 * @author mimic
 */
public interface AsciiRenderer extends AT_Renderer {

    static AsciiRenderer create() {
        return new AsciiRenderer() {
            AT_ColumnWidthCalculator cwc = new CWC_AbsoluteEven();
            String lineSeparator = null;

            @Override
            public AT_ColumnWidthCalculator getCWC() {
                return cwc;
            }

            @Override
            public String getLineSeparator() {
                return lineSeparator;
            }

            @Override
            public AT_Renderer setCWC(AT_ColumnWidthCalculator cwc) {
                if (cwc != null) {
                    this.cwc = cwc;
                }
                return this;
            }

            @Override
            public AT_Renderer setLineSeparator(String separator) {
                if (!StringUtils.isBlank(separator)) {
                    lineSeparator = separator;
                }
                return this;
            }
        };
    }

    @Override
    default Collection<StrBuilder> renderAsCollection(LinkedList<AT_Row> rows, int colNumbers, AT_Context ctx,
            int width) {
        Validate.notNull(rows);
        Validate.notNull(ctx);

        ArrayList<Object> table = new ArrayList<>();
        int[] colWidth = getCWC().calculateColumnWidths(rows, colNumbers, ctx.getTextWidth(width));

        for (AT_Row row : rows) {
            int ruleset;
            switch (row.getStyle()) {
                case NORMAL:
                    ruleset = TA_GridConfig.RULESET_NORMAL;
                    break;
                case STRONG:
                    ruleset = TA_GridConfig.RULESET_STRONG;
                    break;
                case LIGHT:
                    ruleset = TA_GridConfig.RULESET_LIGHT;
                    break;
                case HEAVY:
                    ruleset = TA_GridConfig.RULESET_HEAVY;
                    break;
                case UNKNOWN:
                    throw new AsciiTableException("AT_Renderer: cannot render unknown row style", "table row style set to 'unknown'");
                default:
                    throw new AsciiTableException("AT_Renderer: cannot render unknown row style", "table row style not specified or type not processed");
            }

            switch (row.getType()) {
                case RULE:
                    table.add(ruleset);
                    break;
                case CONTENT:
                    String[][] cAr = new String[colNumbers][];
                    LinkedList<AT_Cell> cells = row.getCells();
                    if (cells == null) {
                        throw new AsciiTableException("cannot render table", "row content (cells) was null");
                    }

                    int length = 0;
                    for (int i = 0; i < cells.size(); i++) {
                        length += colWidth[i];

                        Object content = cells.get(i).getContent();
                        if (content == null) {
                            length++;
                            continue;
                        }

                        int realWidth = length;
                        length -= cells.get(i).getContext().getPaddingLeft();
                        length -= cells.get(i).getContext().getPaddingRight();

                        if (content instanceof RendersToClusterWidth) {
                            cAr[i] = ((RendersToClusterWidth) content).renderAsArray(length);
                        }
                        if (content instanceof DoesRenderToWidth) {
                            cAr[i] = new StrTokenizer(((DoesRenderToWidth) content).render(length))
                                    .setDelimiterChar('\n').setIgnoreEmptyTokens(false).getTokenArray();
                        } else {
                            String text = Object_To_StrBuilder.convert(content).toString();
                            // text = text.replaceAll("\\s+", " ");

                            if (cells.get(i).getContext().getTargetTranslator() != null) {
                                if (cells.get(i).getContext().getTargetTranslator().getCombinedTranslator() != null) {
                                    text = cells.get(i).getContext().getTargetTranslator().getCombinedTranslator()
                                            .translate(text);
                                }
                            } else if (cells.get(i).getContext().getHtmlElementTranslator() != null) {
                                text = cells.get(i).getContext().getHtmlElementTranslator().translateHtmlElements(text);
                            } else if (cells.get(i).getContext().getCharTranslator() != null) {
                                text = cells.get(i).getContext().getCharTranslator().translateCharacters(text);
                            }

                            Collection<StrBuilder> csb = AsciiTextFormatter
                                    .create(length, cells.get(i).getContext().getTextAlignment()
                                            .getMapping(), TextFormat.NONE
                                            .getMapping(), null, null, null, 0, 0, null, 0, 0, null).transform(text);
                            for (StrBuilder sb : csb) {
                                sb.insert(0, new StrBuilder()
                                        .appendPadding(cells.get(i).getContext().getPaddingLeft(), cells.get(i)
                                                .getContext().getPaddingLeftChar()));
                                sb.appendPadding(cells.get(i).getContext().getPaddingRight(), cells.get(i).getContext()
                                        .getPaddingRightChar());
                            }
                            for (int k = 0; k < cells.get(i).getContext().getPaddingTop(); k++) {
                                ((ArrayList<StrBuilder>) csb).add(0, new StrBuilder()
                                        .appendPadding(realWidth, cells.get(i).getContext().getPaddingTopChar()));
                            }
                            for (int k = 0; k < cells.get(i).getContext().getPaddingBottom(); k++) {
                                csb.add(new StrBuilder()
                                        .appendPadding(realWidth, cells.get(i).getContext().getPaddingBottomChar()));
                            }

                            cAr[i] = ClusterElementTransformer.create()
                                    .transform(csb, StrBuilder_To_String.create(), ArrayListStrategy.create())
                                    .toArray(new String[0]);
                        }
                        length = 0;
                    }
                    cAr = Array2D_To_NormalizedArray.create(colNumbers).transform(cAr);
                    cAr = Array2D_To_FlipArray.create().transform(cAr);
                    table.add(Pair.of(ruleset, cAr));
                    break;
                case UNKNOWN:
                    throw new AsciiTableException("AT_Renderer: cannot render unknown row type", "table row type set to 'unknown'");
                default:
                    throw new AsciiTableException("AT_Renderer: cannot render unknown row type", "table row type not specified or type not processed");
            }
        }

        ArrayList<StrBuilder> ret = ctx.getGrid().addGrid(table, ctx.getGridTheme() | ctx.getGridThemeOptions());
        int max = ret.get(0).length() + ctx.getFrameLeftMargin() + ctx.getFrameRightMargin();
        for (StrBuilder sb : ret) {
            sb.insert(0, new StrBuilder().appendPadding(ctx.getFrameLeftMargin(), ctx.getFrameLeftChar()));
            sb.appendPadding(ctx.getFrameRightMargin(), ctx.getFrameRightChar());
        }
        for (int k = 0; k < ctx.getFrameTopMargin(); k++) {
            ret.add(0, new StrBuilder().appendPadding(max, ctx.getFrameTopChar()));
        }
        for (int k = 0; k < ctx.getFrameBottomMargin(); k++) {
            ret.add(new StrBuilder().appendPadding(max, ctx.getFrameBottomChar()));
        }
        for (int k = 0; k < ret.size(); k++) {
            StrBuilder sb = ret.get(k);
            String s = sb.toString().replace((char) 32, AsciiMaker.SPACE_CHAR);
            sb.clear();
            sb.append(s);
            ret.set(k, sb);
        }
        return ret;
    }
}