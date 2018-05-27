package cz.ophite.mimic.vhackos.botnet.shared.utils.ascii;

import de.vandermeer.skb.interfaces.transformers.IsTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

/**
 * Úprava formáteru, který nahrazovat nové řádky z html BR. Bylo nutné opravit, protože to nfungovalo správně.
 *
 * @author mimic
 */
public interface AsciiTextToWrappedFormat extends IsTransformer<String, Pair<ArrayList<String>, ArrayList<String>>> {

    String LINEBREAK = "\0"; // <br />

    default int getWidth() {
        return 80;
    }

    default Pair<Integer, Integer> getTopSettings() {
        return null;
    }

    @Override
    default Pair<ArrayList<String>, ArrayList<String>> transform(String input) {
        Validate.notBlank(input);
        Validate.isTrue(getWidth() > 0);

        ArrayList<String> topList = new ArrayList<>();
        ArrayList<String> bottomList = new ArrayList<>();

        int count;

        String text = StringUtils.replacePattern(input, "\\r\\n|\\r|\\n", LINEBREAK);
        text = StringUtils.replace(text, "<br>", LINEBREAK);
        text = StringUtils.replace(text, "<br/>", LINEBREAK);

        StrBuilder sb = new StrBuilder(text);
        if (getTopSettings() != null) {
            Validate.notNull(getTopSettings().getLeft());
            Validate.notNull(getTopSettings().getRight());
            Validate.isTrue(getTopSettings().getLeft() > 0);
            Validate.isTrue(getTopSettings().getRight() > 0);

            int topLines = getTopSettings().getLeft();
            int topWidth = getTopSettings().getRight();
            count = 0;

            while (sb.size() > 0 && topLines > 0 && count++ < 200) {
                if (sb.startsWith(LINEBREAK)) {
                    sb.replaceFirst(LINEBREAK, "");
                }
                String s;
                boolean wln = false;
                if (sb.indexOf(LINEBREAK) > 0) {
                    s = sb.substring(0, sb.indexOf(LINEBREAK));
                    wln = true;
                } else {
                    s = sb.toString();
                }
                String wrap = WordUtils.wrap(s, topWidth, LINEBREAK, true);
                StrTokenizer tok = new StrTokenizer(wrap, LINEBREAK).setIgnoreEmptyTokens(false);
                String[] ar = tok.getTokenArray();
                if (ar.length <= topLines) {
                    for (String str : ar) {
                        topList.add(str.trim());
                    }
                    if (wln) {
                        sb.replace(0, sb.indexOf(LINEBREAK) + LINEBREAK.length(), "");
                    } else {
                        sb.clear();
                    }
                    topLines = 0;
                } else {
                    StrBuilder replace = new StrBuilder();
                    for (int i = 0; i < topLines; i++) {
                        topList.add(ar[i].trim());
                        replace.appendSeparator(' ').append(ar[i]);
                    }
                    if (wln) {
                        replace.append(LINEBREAK);
                    }
                    sb.replaceFirst(replace.toString(), "");
                    topLines = 0;
                }
            }
        }

        count = 0;
        while (sb.size() > 0 && count++ < 200) {
            if (sb.startsWith(LINEBREAK)) {
                sb.replaceFirst(LINEBREAK, "");
            }
            String s;
            if (sb.indexOf(LINEBREAK) > 0) {
                s = sb.substring(0, sb.indexOf(LINEBREAK));
                sb.replace(0, sb.indexOf(LINEBREAK) + LINEBREAK.length(), "");
            } else {
                s = sb.toString();
                sb.clear();
            }
            s = WordUtils.wrap(s, getWidth(), LINEBREAK, true);
            StrTokenizer tok = new StrTokenizer(s, LINEBREAK).setIgnoreEmptyTokens(false);
            for (String str : tok.getTokenArray()) {
                bottomList.add(str.trim());
            }
        }

        return Pair.of(topList, bottomList);
    }

    static AsciiTextToWrappedFormat create(int width) {
        return new AsciiTextToWrappedFormat() {
            @Override
            public int getWidth() {
                return width;
            }
        };
    }

    static AsciiTextToWrappedFormat create(int width, Pair<Integer, Integer> top) {
        return new AsciiTextToWrappedFormat() {
            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public Pair<Integer, Integer> getTopSettings() {
                return top;
            }
        };
    }

    static Pair<ArrayList<String>, ArrayList<String>> convert(String text, int width) {
        return create(width).transform(text);
    }

    static Pair<ArrayList<String>, ArrayList<String>> convert(String text, int width, Pair<Integer, Integer> top) {
        return create(width, top).transform(text);
    }
}
