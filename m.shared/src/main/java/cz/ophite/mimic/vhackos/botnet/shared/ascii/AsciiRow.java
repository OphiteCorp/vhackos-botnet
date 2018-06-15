package cz.ophite.mimic.vhackos.botnet.shared.ascii;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Označí řádek pro zpracování ASCII tabulkou.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface AsciiRow {

    /**
     * Název klíče. Pokud nebude vyplněn, tak se vezme název fieldu.
     */
    String value() default "";

    /**
     * Nastaví vlastní konverter.
     */
    Class<? extends IAsciiConverter> converter() default IAsciiConverter.class;
}
