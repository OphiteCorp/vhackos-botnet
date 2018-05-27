package cz.ophite.mimic.vhackos.botnet.shared.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Označí metodu jako příkaz a tím půjde zavolat externě.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Command {

    /**
     * Název příkazu. Pokud nebude vyplněn, bude použit název metody.
     */
    String value() default "";

    /**
     * Název kategorie, do které příkaz spadá. Není povinné.
     */
    String category() default "";

    /**
     * Komentář příkazu.
     */
    String comment() default "";
}
