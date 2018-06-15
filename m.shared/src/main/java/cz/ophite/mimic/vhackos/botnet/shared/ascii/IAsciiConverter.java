package cz.ophite.mimic.vhackos.botnet.shared.ascii;

/**
 * Rozhraní, kterí pouze definuje převodník pro správnou instanci.
 *
 * @author mimic
 */
public interface IAsciiConverter {

    /**
     * Konverze původní hodnoty na jinou.
     */
    Object convert(Object value);
}
