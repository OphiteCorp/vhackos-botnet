package cz.ophite.mimic.vhackos.botnet.api.dto;

/**
 * Informace pro proxy serveru.
 *
 * @author mimic
 */
public final class ProxyData {

    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", ip, port);
    }
}
