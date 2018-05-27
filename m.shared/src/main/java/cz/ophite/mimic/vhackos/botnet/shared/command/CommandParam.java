package cz.ophite.mimic.vhackos.botnet.shared.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Nadefinuje rozšířený vlastnosti parametru metody příkazu.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface CommandParam {

    /**
     * Vlastní název parametru. Pokud nebude vyplněn, bude použit název datového typu.
     */
    String value();
}
