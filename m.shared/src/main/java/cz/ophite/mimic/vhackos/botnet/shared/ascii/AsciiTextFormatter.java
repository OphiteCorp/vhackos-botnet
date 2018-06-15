package cz.ophite.mimic.vhackos.botnet.shared.ascii;

import de.vandermeer.skb.interfaces.strategies.IsCollectionStrategy;
import de.vandermeer.skb.interfaces.strategies.collections.list.ArrayListStrategy;
import de.vandermeer.skb.interfaces.transformers.ClusterElementTransformer;
import de.vandermeer.skb.interfaces.transformers.IsTransformer;
import de.vandermeer.skb.interfaces.transformers.Transformer;
import de.vandermeer.skb.interfaces.transformers.textformat.String_To_Centered;
import de.vandermeer.skb.interfaces.transformers.textformat.String_To_LeftPadded;
import de.vandermeer.skb.interfaces.transformers.textformat.String_To_NoWs;
import de.vandermeer.skb.interfaces.transformers.textformat.String_To_RightPadded;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Vlastní text formáter, protože původní odebírá duplicitní znaky (např. mezery).
 *
 * @author mimic
 */
public interface AsciiTextFormatter extends IsTransformer<String, Collection<StrBuilder>> {

    int ALIGN_LEFT = 1;
    int ALIGN_CENTER = 2;
    int ALIGN_RIGHT = 3;
    int ALIGN_JUSTIFIED = 4;
    int ALIGN_JUSTIFIED_LEFT = 5;
    int ALIGN_JUSTIFIED_RIGHT = 6;
    int FORMAT_NONE = 100;
    int FORMAT_FIRST_LINE = 101;
    int FORMAT_HANGING_PARAGRAPH = 102;
    int FORMAT_FIRSTLINE_AND_HANGINGPARAGRAPH = 103;
    int FORMAT_DROPCAP = 120;
    int FORMAT_DROPCAP_WITH_PADDING = 121;
    int DEFAULT_TEXT_WIDTH = 80;
    char DEFAULT_LEFT_PADDING_CHARACTER = ' ';
    char DEFAULT_RIGHT_PADDING_CHARACTER = ' ';
    char DEFAULT_INNER_WHITESPACE_CHARACTER = ' ';
    int DEFAULT_HANGING_INDENTATION = 4;
    int DEFAULT_FIRSTLINE_INDENTATION = 4;
    int DEFAULT_CHARS_BETWEEN_DROPCAP_AND_TEXT = 3;
    int DEFAULT_LINES_AFTER_DROPCAP = 1;

    IsCollectionStrategy<?, StrBuilder> DEFAULT_COLLECTION_STRATEGY = ArrayListStrategy.create();

    default int getAlignment() {
        return ALIGN_JUSTIFIED_LEFT;
    }

    default int getFormat() {
        return FORMAT_NONE;
    }

    default Character getInnerWsChar() {
        return DEFAULT_INNER_WHITESPACE_CHARACTER;
    }

    default Character getLeftPaddingChar() {
        return DEFAULT_LEFT_PADDING_CHARACTER;
    }

    default Character getRightPaddingChar() {
        return DEFAULT_RIGHT_PADDING_CHARACTER;
    }

    default int getTextWidth() {
        return DEFAULT_TEXT_WIDTH;
    }

    default IsCollectionStrategy<?, StrBuilder> getCollectionStrategy() {
        return DEFAULT_COLLECTION_STRATEGY;
    }

    default int getHangingIndentation() {
        return DEFAULT_HANGING_INDENTATION;
    }

    default int getFirstlineIndentation() {
        return DEFAULT_FIRSTLINE_INDENTATION;
    }

    default int getCharsBetweenDroppcapAndText() {
        return DEFAULT_CHARS_BETWEEN_DROPCAP_AND_TEXT;
    }

    default int getLinesAfterDropcap() {
        return DEFAULT_LINES_AFTER_DROPCAP;
    }

    default Collection<StrBuilder> transform(StrBuilder sb) {
        Validate.notNull(sb);
        return transform(sb.toString());
    }

    String[] getDropCap();

    static AsciiTextFormatter create(int textWidth, int alignment, int format, Character leftPadding,
            Character rightPadding, Character innerWS, int hangingIndentation, int firstlineIndentation,
            String[] dropCap, int charsBetweenDroppcapAndText, int linesAfterDropcap,
            IsCollectionStrategy<?, StrBuilder> strategy) {
        return new AsciiTextFormatter() {
            @Override
            public int getAlignment() {
                return alignment;
            }

            @Override
            public int getFormat() {
                return format;
            }

            @Override
            public int getTextWidth() {
                return textWidth;
            }

            @Override
            public Character getInnerWsChar() {
                return (innerWS == null) ? AsciiTextFormatter.super.getInnerWsChar() : innerWS;
            }

            @Override
            public Character getLeftPaddingChar() {
                return (leftPadding == null) ? AsciiTextFormatter.super.getLeftPaddingChar() : leftPadding;
            }

            @Override
            public Character getRightPaddingChar() {
                return (rightPadding == null) ? AsciiTextFormatter.super.getRightPaddingChar() : rightPadding;
            }

            @Override
            public IsCollectionStrategy<?, StrBuilder> getCollectionStrategy() {
                return (strategy == null) ? AsciiTextFormatter.super.getCollectionStrategy() : strategy;
            }

            @Override
            public int getHangingIndentation() {
                return (hangingIndentation < 1) ? AsciiTextFormatter.super.getHangingIndentation() : hangingIndentation;
            }

            @Override
            public int getFirstlineIndentation() {
                return (firstlineIndentation < 1) ? AsciiTextFormatter.super
                        .getFirstlineIndentation() : firstlineIndentation;
            }

            @Override
            public int getCharsBetweenDroppcapAndText() {
                return (charsBetweenDroppcapAndText < 1) ? AsciiTextFormatter.super
                        .getCharsBetweenDroppcapAndText() : charsBetweenDroppcapAndText;
            }

            @Override
            public int getLinesAfterDropcap() {
                return (linesAfterDropcap < 1) ? AsciiTextFormatter.super.getLinesAfterDropcap() : linesAfterDropcap;
            }

            @Override
            public String[] getDropCap() {
                return dropCap;
            }
        };
    }

    @Override
    default Collection<StrBuilder> transform(String s) {
        IsTransformer.super.transform(s);
        Validate.validState(ArrayUtils.contains(new int[]{
                ALIGN_LEFT, ALIGN_RIGHT, ALIGN_CENTER, ALIGN_JUSTIFIED, ALIGN_JUSTIFIED_LEFT,
                ALIGN_JUSTIFIED_RIGHT }, getAlignment()), "unknown alignment <" + getAlignment() + ">");
        Validate.validState(ArrayUtils.contains(new int[]{
                FORMAT_NONE, FORMAT_HANGING_PARAGRAPH, FORMAT_FIRST_LINE, FORMAT_FIRSTLINE_AND_HANGINGPARAGRAPH,
                FORMAT_DROPCAP, FORMAT_DROPCAP_WITH_PADDING }, getFormat()), "unknown format <" + getFormat() + ">");
        Validate.validState(getTextWidth() > 0, "text width is less than 1, was <" + getTextWidth() + ">");
        Validate.notNull(getInnerWsChar());
        Validate.notNull(getLeftPaddingChar());
        Validate.notNull(getRightPaddingChar());
        Validate.notNull(getCollectionStrategy());
        Validate.validState(getHangingIndentation() > 0, "hanging paragraph indentation was less than null, setting was <" + getHangingIndentation() + ">");
        Validate.validState(getFirstlineIndentation() > 0, "first line indentation was less than null, setting was <" + getFirstlineIndentation() + ">");
        Validate.validState(getCharsBetweenDroppcapAndText() > 0, "characters between dropped capital letter and text lines was less than 1, setting was <" + getCharsBetweenDroppcapAndText() + ">");
        Validate.validState(getLinesAfterDropcap() > 0, "lines added after a dropped capital letter was less than 1, setting was <" + getLinesAfterDropcap() + ">");

        Collection<StrBuilder> ret = getCollectionStrategy().get();

        if (StringUtils.isBlank(s)) {
            ret.add(new StrBuilder().appendPadding(getTextWidth(), ' '));
            return ret;
        }

        if (getFormat() == FORMAT_DROPCAP || getFormat() == FORMAT_DROPCAP_WITH_PADDING) {
            Validate.notNull(getDropCap());
            Validate.noNullElements(getDropCap());
            int l = 0;
            for (String ds : getDropCap()) {
                if (l != 0) {
                    Validate.validState(l == ds
                            .length(), "dropped capital letter has some variations in length in the array, not alowed");
                }
                l = ds.length();
            }
        }

        String text = String_To_NoWs.convert(s);

        Pair<ArrayList<String>, ArrayList<String>> pair = null;
        int topWidth = 0;
        int bottomWidth = 0;
        switch (getFormat()) {
            case FORMAT_NONE:
                topWidth = bottomWidth = getTextWidth();
                pair = AsciiTextToWrappedFormat.convert(text, topWidth);
                Validate.isTrue(pair.getLeft().size() == 0);
                break;
            case FORMAT_HANGING_PARAGRAPH:
                topWidth = getTextWidth();
                bottomWidth = getTextWidth() - getHangingIndentation();
                pair = AsciiTextToWrappedFormat.convert(text, bottomWidth, Pair.of(1, topWidth));
                Validate.isTrue(pair.getLeft().size() == 1);
                break;
            case FORMAT_FIRST_LINE:
                topWidth = getTextWidth() - getFirstlineIndentation();
                bottomWidth = getTextWidth();
                pair = AsciiTextToWrappedFormat.convert(text, bottomWidth, Pair.of(1, topWidth));
                Validate.isTrue(pair.getLeft().size() == 1);
                break;
            case FORMAT_FIRSTLINE_AND_HANGINGPARAGRAPH:
                topWidth = getTextWidth() - getFirstlineIndentation();
                bottomWidth = getTextWidth() - getHangingIndentation();
                pair = AsciiTextToWrappedFormat.convert(text, bottomWidth, Pair.of(1, topWidth));
                Validate.isTrue(pair.getLeft().size() == 1);
                break;
            case FORMAT_DROPCAP:
                topWidth = getTextWidth() - getDropCap()[0].length() - 1;
                bottomWidth = getTextWidth();
                pair = AsciiTextToWrappedFormat.convert(text.substring(1), bottomWidth, Pair
                        .of(getDropCap().length + getLinesAfterDropcap(), topWidth));
                Validate.isTrue(pair.getLeft().size() == getDropCap().length + 1);
                break;
            case FORMAT_DROPCAP_WITH_PADDING:
                topWidth = getTextWidth() - getDropCap()[0].length() - getCharsBetweenDroppcapAndText();
                bottomWidth = getTextWidth();
                pair = AsciiTextToWrappedFormat.convert(text.substring(1), bottomWidth, Pair
                        .of(getDropCap().length + getLinesAfterDropcap(), topWidth));
                Validate.isTrue(pair.getLeft().size() == getDropCap().length + 1);
        }

        Transformer<String, StrBuilder> topTr = null;
        Transformer<String, StrBuilder> bottomTr = null;
        switch (getAlignment()) {
            case ALIGN_LEFT:
                topTr = String_To_LeftPadded.create(topWidth, getRightPaddingChar(), getInnerWsChar(), null);
                bottomTr = String_To_LeftPadded.create(bottomWidth, getRightPaddingChar(), getInnerWsChar(), null);
                break;
            case ALIGN_RIGHT:
                topTr = String_To_RightPadded.create(topWidth, getLeftPaddingChar(), getInnerWsChar(), null);
                bottomTr = String_To_RightPadded.create(bottomWidth, getLeftPaddingChar(), getInnerWsChar(), null);
                break;
            case ALIGN_CENTER:
                topTr = String_To_Centered
                        .create(topWidth, getLeftPaddingChar(), getRightPaddingChar(), getInnerWsChar(), null);
                bottomTr = String_To_Centered
                        .create(bottomWidth, getLeftPaddingChar(), getRightPaddingChar(), getInnerWsChar(), null);
                break;
            case ALIGN_JUSTIFIED:
            case ALIGN_JUSTIFIED_LEFT:
            case ALIGN_JUSTIFIED_RIGHT:
                topTr = AsciiStringToJustified.create(topWidth, getInnerWsChar(), null);
                bottomTr = AsciiStringToJustified.create(bottomWidth, getInnerWsChar(), null);
                break;
        }

        Collection<StrBuilder> top = ClusterElementTransformer.create()
                .transform(pair.getLeft(), topTr, getCollectionStrategy());
        Collection<StrBuilder> bottom = ClusterElementTransformer.create()
                .transform(pair.getRight(), bottomTr, getCollectionStrategy());

        if (bottom.size() > 0 && (getAlignment() == ALIGN_JUSTIFIED_LEFT || getAlignment() == ALIGN_JUSTIFIED_RIGHT)) {
            Object[] objAr = bottom.toArray();
            Object line = objAr[objAr.length - 1];
            bottom.remove(objAr[objAr.length - 1]);

            String lineString = line.toString().replaceAll(getInnerWsChar().toString(), " ");
            lineString = lineString.replace((char) 32, AsciiMaker.SPACE_CHAR);

            if (getAlignment() == ALIGN_JUSTIFIED_LEFT) {
                bottom.add(String_To_LeftPadded.convert(lineString, bottomWidth, getRightPaddingChar()));
            }
            if (getAlignment() == ALIGN_JUSTIFIED_RIGHT) {
                bottom.add(String_To_RightPadded.convert(lineString, bottomWidth, getLeftPaddingChar()));
            }
        }

        switch (getFormat()) {
            case FORMAT_NONE:
                ret.addAll(top);
                ret.addAll(bottom);
                break;
            case FORMAT_HANGING_PARAGRAPH:
                ret.addAll(top);
                for (StrBuilder b : bottom) {
                    ret.add(new StrBuilder().appendPadding(getHangingIndentation(), getLeftPaddingChar()).append(b));
                }
                break;
            case FORMAT_FIRST_LINE:
                for (StrBuilder t : top) {
                    ret.add(new StrBuilder().appendPadding(getFirstlineIndentation(), getLeftPaddingChar()).append(t));
                }
                ret.addAll(bottom);
                break;
            case FORMAT_FIRSTLINE_AND_HANGINGPARAGRAPH:
                for (StrBuilder t : top) {
                    ret.add(new StrBuilder().appendPadding(getFirstlineIndentation(), getLeftPaddingChar()).append(t));
                }
                for (StrBuilder b : bottom) {
                    ret.add(new StrBuilder().appendPadding(getHangingIndentation(), getLeftPaddingChar()).append(b));
                }
                break;
            case FORMAT_DROPCAP:
                int count = 0;
                for (StrBuilder t : top) {
                    if (count < getDropCap().length) {
                        ret.add(new StrBuilder().append(getDropCap()[count]).append(' ').append(t));
                    } else {
                        ret.add(new StrBuilder().appendPadding(getDropCap()[0].length(), ' ').append(' ').append(t));
                    }
                    count++;
                }
                ret.addAll(bottom);
                break;
            case FORMAT_DROPCAP_WITH_PADDING:
                count = 0;
                for (StrBuilder t : top) {
                    if (count < getDropCap().length) {
                        ret.add(new StrBuilder().append(getDropCap()[count])
                                .appendPadding(getCharsBetweenDroppcapAndText(), ' ').append(t));
                    } else {
                        ret.add(new StrBuilder().appendPadding(getDropCap()[0].length(), ' ')
                                .appendPadding(getCharsBetweenDroppcapAndText(), ' ').append(t));
                    }
                    count++;
                }
                ret.addAll(bottom);
        }

        return ret;
    }

    static Collection<StrBuilder> left(String text, int textWidth) {
        return left(text, textWidth, FORMAT_NONE, null, null, null);
    }

    static Collection<StrBuilder> left(String text, int textWidth, int format) {
        return left(text, textWidth, format, null, null, null);
    }

    static Collection<StrBuilder> left(String text, int textWidth, int format, Character padding) {
        return left(text, textWidth, format, padding, null, null);
    }

    static Collection<StrBuilder> left(String text, int textWidth, int format, Character padding, Character innerWS) {
        return left(text, textWidth, format, padding, innerWS, null);
    }

    static Collection<StrBuilder> left(String text, int textWidth, int format, Character padding, Character innerWS,
            IsCollectionStrategy<?, StrBuilder> strategy) {
        return create(textWidth, ALIGN_LEFT, format, null, padding, innerWS, 0, 0, null, 0, 0, strategy)
                .transform(text);
    }

    static Collection<StrBuilder> center(String text, int textWidth) {
        return center(text, textWidth, FORMAT_NONE, null, null, null, null);
    }

    static Collection<StrBuilder> center(String text, int textWidth, int format) {
        return center(text, textWidth, format, null, null, null, null);
    }

    static Collection<StrBuilder> center(String text, int textWidth, int format, Character padding) {
        return center(text, textWidth, format, padding, padding, null, null);
    }

    static Collection<StrBuilder> center(String text, int textWidth, int format, Character leftPadding,
            Character rightPadding) {
        return center(text, textWidth, format, leftPadding, rightPadding, null, null);
    }

    static Collection<StrBuilder> center(String text, int textWidth, int format, Character leftPadding,
            Character rightPadding, Character innerWS) {
        return center(text, textWidth, format, leftPadding, rightPadding, innerWS, null);
    }

    static Collection<StrBuilder> center(String text, int textWidth, int format, Character leftPadding,
            Character rightPadding, Character innerWS, IsCollectionStrategy<?, StrBuilder> strategy) {
        return create(textWidth, ALIGN_CENTER, format, leftPadding, rightPadding, innerWS, 0, 0, null, 0, 0, strategy)
                .transform(text);
    }

    static Collection<StrBuilder> right(String text, int textWidth) {
        return right(text, textWidth, FORMAT_NONE, null, null, null);
    }

    static Collection<StrBuilder> right(String text, int textWidth, int format) {
        return right(text, textWidth, format, null, null, null);
    }

    static Collection<StrBuilder> right(String text, int textWidth, int format, Character padding) {
        return right(text, textWidth, format, padding, null, null);
    }

    static Collection<StrBuilder> right(String text, int textWidth, int format, Character padding, Character innerWS) {
        return right(text, textWidth, format, padding, innerWS, null);
    }

    static Collection<StrBuilder> right(String text, int textWidth, int format, Character padding, Character innerWS,
            IsCollectionStrategy<?, StrBuilder> strategy) {
        return create(textWidth, ALIGN_RIGHT, format, padding, null, innerWS, 0, 0, null, 0, 0, strategy)
                .transform(text);
    }

    static Collection<StrBuilder> justified(String text, int textWidth) {
        return justified(text, textWidth, FORMAT_NONE, null, null);
    }

    static Collection<StrBuilder> justified(String text, int textWidth, int format) {
        return justified(text, textWidth, format, null, null);
    }

    static Collection<StrBuilder> justified(String text, int textWidth, int format, Character innerWS) {
        return justified(text, textWidth, format, innerWS, null);
    }

    static Collection<StrBuilder> justified(String text, int textWidth, int format, Character innerWS,
            IsCollectionStrategy<?, StrBuilder> strategy) {
        return create(textWidth, ALIGN_JUSTIFIED, format, null, null, innerWS, 0, 0, null, 0, 0, strategy)
                .transform(text);
    }

    static Collection<StrBuilder> justifiedLeft(String text, int textWidth) {
        return justifiedLeft(text, textWidth, FORMAT_NONE, null, null, null);
    }

    static Collection<StrBuilder> justifiedLeft(String text, int textWidth, int format) {
        return justifiedLeft(text, textWidth, format, null, null, null);
    }

    static Collection<StrBuilder> justifiedLeft(String text, int textWidth, int format, Character padding) {
        return justifiedLeft(text, textWidth, format, padding, null, null);
    }

    static Collection<StrBuilder> justifiedLeft(String text, int textWidth, int format, Character padding,
            Character innerWS) {
        return justifiedLeft(text, textWidth, format, padding, innerWS, null);
    }

    static Collection<StrBuilder> justifiedLeft(String text, int textWidth, int format, Character padding,
            Character innerWS, IsCollectionStrategy<?, StrBuilder> strategy) {
        return create(textWidth, ALIGN_JUSTIFIED_LEFT, format, null, padding, innerWS, 0, 0, null, 0, 0, strategy)
                .transform(text);
    }

    static Collection<StrBuilder> justifiedRight(String text, int textWidth) {
        return justifiedRight(text, textWidth, FORMAT_NONE, null, null, null);
    }

    static Collection<StrBuilder> justifiedRight(String text, int textWidth, int format) {
        return justifiedRight(text, textWidth, format, null, null, null);
    }

    static Collection<StrBuilder> justifiedRight(String text, int textWidth, int format, Character padding) {
        return justifiedRight(text, textWidth, format, padding, null, null);
    }

    static Collection<StrBuilder> justifiedRight(String text, int textWidth, int format, Character padding,
            Character innerWS) {
        return justifiedRight(text, textWidth, format, padding, innerWS, null);
    }

    static Collection<StrBuilder> justifiedRight(String text, int textWidth, int format, Character padding,
            Character innerWS, IsCollectionStrategy<?, StrBuilder> strategy) {
        return create(textWidth, ALIGN_JUSTIFIED_RIGHT, format, padding, null, innerWS, 0, 0, null, 0, 0, strategy)
                .transform(text);
    }
}
