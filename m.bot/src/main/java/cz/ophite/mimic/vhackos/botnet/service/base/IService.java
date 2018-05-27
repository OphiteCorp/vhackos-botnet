package cz.ophite.mimic.vhackos.botnet.service.base;

/**
 * Základní rozhraní služby.
 *
 * @author mimic
 */
public interface IService {

    String SERVICE_UPDATE = "update";
    String SERVICE_MINER = "miner";
    String SERVICE_MALWARE = "malware";
    String SERVICE_SERVER = "server";
    String SERVICE_STORE = "store";
    String SERVICE_BOOSTER = "booster";
    String SERVICE_MISSION = "mission";
    String SERVICE_NETWORK = "network";
    String SERVICE_NETWORK_SCAN = "netscan";

    /**
     * Spustí službu asynchronně.
     */
    boolean start();

    /**
     * Spustí službu (je možné spustit službu i synchronně).
     */
    boolean start(boolean async);

    /**
     * Zastaví službu.
     */
    boolean stop();

    /**
     * Detekuje, zda služba již běží.
     */
    boolean isRunning();

    /**
     * Popis služby.
     */
    default String getDescription() {
        return "";
    }
}
