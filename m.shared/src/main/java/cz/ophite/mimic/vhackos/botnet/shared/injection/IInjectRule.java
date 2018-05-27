package cz.ophite.mimic.vhackos.botnet.shared.injection;

/**
 * Pravidlo pro vytvoření instance třídy. Používá se přimárně v případě, že třídy obsahuje konstruktor bez parametrů.
 *
 * @author mimic
 */
public interface IInjectRule {

    /**
     * Vytvoří instanci třídy.
     */
    Object createInstance(Class clazz) throws Exception;
}
