package cz.ophite.mimic.vhackos.botnet.api.net.response.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Přiřadí fieldu návratový klíč odpovědi z requestu pro přemapování.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ResponseKey {

    /**
     * Název parametru v odpovědi.
     */
    String value();
}
