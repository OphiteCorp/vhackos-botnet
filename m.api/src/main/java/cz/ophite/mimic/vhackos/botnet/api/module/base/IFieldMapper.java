package cz.ophite.mimic.vhackos.botnet.api.module.base;

import java.lang.reflect.Field;

/**
 * Rozhraní definující vlastní implementaci pro přemapování požadavku odpovědi na jiný typ než primitivní.
 *
 * @author mimic
 */
public interface IFieldMapper {

    /**
     * Konvertuje data odpovědi na daný typ fieldu.
     */
    Object convert(Field field, Object responseData);
}
