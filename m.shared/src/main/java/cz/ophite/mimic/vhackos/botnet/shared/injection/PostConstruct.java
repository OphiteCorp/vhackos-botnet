package cz.ophite.mimic.vhackos.botnet.shared.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Označí metodu, která se automaticky zavolá po vytvoření instance třídy. Tato metoda musí být bez parametru.
 *
 * @author mimic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@interface PostConstruct {

}
