package cz.ophite.mimic.vhackos.botnet.dto;

import com.google.gson.annotations.SerializedName;
import cz.ophite.mimic.vhackos.botnet.api.dto.ConnectionData;

/**
 * Obsahuje cache data, která se
 *
 * @author mimic
 */
public final class CacheData {

    public static final String FILE_NAME = "botnet_cache.json";

    @SerializedName("connection_data")
    private ConnectionData connectionData;

    public ConnectionData getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(ConnectionData connectionData) {
        this.connectionData = connectionData;
    }
}
