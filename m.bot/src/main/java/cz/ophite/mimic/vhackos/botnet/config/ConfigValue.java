package cz.ophite.mimic.vhackos.botnet.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definice konfigurační hodnoty.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@interface ConfigValue {

    /**
     * Klíč konfigurační hodnoty.
     */
    String value();

    /**
     * Výchozí hodnota (pokud nebude vyplněna v konfiguraci, tak se použije tato).
     */
    String defaultValue() default "";

    /**
     * Komentář hodnoty.
     */
    String comment() default "";

    /**
     * Může být konfigurační hodnota prázdná?
     */
    boolean canBeEmpty() default false;
}
