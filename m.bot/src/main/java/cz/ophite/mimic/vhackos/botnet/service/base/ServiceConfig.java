package cz.ophite.mimic.vhackos.botnet.service.base;

/**
 * Konfigurace spuštění služby.
 *
 * @author mimic
 */
public final class ServiceConfig {

    private boolean async;
    private boolean firstRunSync;

    boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    boolean isFirstRunSync() {
        return firstRunSync;
    }

    public void setFirstRunSync(boolean firstRunSync) {
        this.firstRunSync = firstRunSync;
    }
}
