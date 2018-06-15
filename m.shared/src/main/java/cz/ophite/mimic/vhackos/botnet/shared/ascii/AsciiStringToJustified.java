package cz.ophite.mimic.vhackos.botnet.shared.ascii;

import de.vandermeer.skb.interfaces.transformers.IsTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

/**
 * Vlastní pravidlo pro zarovnání textu. Je stejné jako původní jen je opravena fatální chyba, díky které to skončí v
 * nekonečné smyčce.
 *
 * @author mimic
 */
public interface AsciiStringToJustified extends IsTransformer<String, StrBuilder> {

    int DEFAULT_LENGTH = 80;
    char DEFAULT_INNER_WHITESPACE_CHARACTER = ' ';

    default int getLength() {
        return DEFAULT_LENGTH;
    }

    default Character getInnerWsChar() {
        return DEFAULT_INNER_WHITESPACE_CHARACTER;
    }

    default StrBuilder getBuilderForAppend() {
        return null;
    }

    @Override
    default StrBuilder transform(String s) {
        IsTransformer.super.transform(s);
        StrBuilder ret = (getBuilderForAppend() == null) ? new StrBuilder(getLength()) : getBuilderForAppend();

        String[] ar = StringUtils.split((s == null) ? "" : s);
        int length = 0;
        for (String str : ar) {
            length += str.length();
        }

        int l = ((ar.length - 1) == 0) ? 1 : (ar.length - 1);
        int first = ((getLength() - length) / l) * (ar.length - 1);
        // oprava podmínky, protože pokud vstupní string je prázdný, tak to skončí v nekonečné smyčce, protože first
        // je větší než 0, ale ar je 0
        while (first > 0 && ar.length > 0) {
            for (int i = 0; i < ar.length - 1; i++) {
                if (first != 0) {
                    ar[i] += getInnerWsChar();
                    first--;
                }
            }
        }

        int second = (getLength() - length) % l;
        // oprava podmínky, protože pokud vstupní string je prázdný, tak to skončí v nekonečné smyčce, protože second
        // je větší než 0, ale ar je 0
        while (second > 0 && ar.length - 2 > 0) {
            for (int i = ar.length - 2; i > 0; i--) {
                if (second != 0) {
                    ar[i] += getInnerWsChar();
                    second--;
                }
            }
        }
        ret.append(StringUtils.join(ar));
        while (ret.length() < getLength()) {
            ret.append(' ');
        }
        return ret;
    }

    static AsciiStringToJustified create(int length, Character innerWsChar, StrBuilder builder) {
        Validate.validState(length > 0, "cannot work with lenght of less than 1");
        return new AsciiStringToJustified() {
            @Override
            public int getLength() {
                return (length < 1) ? AsciiStringToJustified.super.getLength() : length;
            }

            @Override
            public StrBuilder getBuilderForAppend() {
                return builder;
            }

            @Override
            public Character getInnerWsChar() {
                return (innerWsChar == null) ? AsciiStringToJustified.super.getInnerWsChar() : innerWsChar;
            }
        };
    }

    static StrBuilder convert(String s, int length) {
        return AsciiStringToJustified.create(length, null, null).transform(s);
    }

    static StrBuilder convert(String s, int length, Character innerWsChar) {
        return AsciiStringToJustified.create(length, innerWsChar, null).transform(s);
    }

    static StrBuilder convert(String s, int length, StrBuilder builder) {
        return AsciiStringToJustified.create(length, null, builder).transform(s);
    }

    static StrBuilder convert(String s, int length, Character innerWsChar, StrBuilder builder) {
        return AsciiStringToJustified.create(length, innerWsChar, builder).transform(s);
    }
}
