package cz.ophite.mimic.vhackos.botnet.shared.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Označí instanční proměnnou a pokusí se jí automaticky na inicializovat.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Autowired {

}
