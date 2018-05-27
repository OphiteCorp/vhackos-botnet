package cz.ophite.mimic.vhackos.botnet.service.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Označí službu a přidá ji další vlastnosti.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EndpointService {

    /**
     * Název služby.
     */
    String value();
}
