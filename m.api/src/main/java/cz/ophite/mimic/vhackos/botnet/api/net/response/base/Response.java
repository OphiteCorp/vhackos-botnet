package cz.ophite.mimic.vhackos.botnet.api.net.response.base;

/**
 * Odpověď ze serveru.
 *
 * @author mimic
 */
public abstract class Response {

    @ResponseKey("result")
    private String result;
    public static final String P_RESULT = "result";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
