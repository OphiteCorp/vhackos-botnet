package cz.ophite.mimic.vhackos.botnet.shared.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definuje třídu, která má být automaticky zahrnuta mezi závislosti.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Inject {

    /**
     * Název závislosti. Pokud nebude vyplněn, tak se použije název třídy.
     */
    String value() default "";

    /**
     * Má se závislost do načíst později?.
     */
    boolean lazy() default false;
}
