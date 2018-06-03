package cz.ophite.mimic.vhackos.botnet.api.net;

import com.google.gson.Gson;
import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.dto.ProxyData;
import cz.ophite.mimic.vhackos.botnet.api.exception.*;
import cz.ophite.mimic.vhackos.botnet.api.module.base.IModule;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.IOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;
import cz.ophite.mimic.vhackos.botnet.shared.json.Json;
import cz.ophite.mimic.vhackos.botnet.shared.utils.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

/**
 * Vytvoří request pro opcode.
 *
 * @author mimic
 */
public final class OpcodeRequest {

    private static final Logger LOG = LoggerFactory.getLogger(OpcodeRequest.class);

    // definuje URL na API herního serveru
    private static final String REST_URI = "https://api.vhack.cc/mobile/" + IBotnet.REST_API_VERSION;

    private static final Gson GSON = new Gson();
    private static final byte EQUALS_SIGN = (byte) 61;
    private static final byte[] WEBSAFE_ALPHABET = new byte[]{
            (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73,
            (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82,
            (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97,
            (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106,
            (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115,
            (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49,
            (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45,
            (byte) 95 };

    private final IOpcode opcode;

    public OpcodeRequest(IOpcode opcode) {
        this.opcode = opcode;
    }

    /**
     * Odešle opcode na server.
     */
    public Map<String, Object> send(IModule module) {
        LOG.debug("Sending request to opcode '{}' with opcode value: {}", opcode.getTarget(), opcode.getOpcodeValue());

        var params = opcode.getParams();
        if (!opcode.isStandaloneOpcode()) {
            injectInitParams(params, module);
        }
        LOG.debug("Params: {}", params);

        // obsahuje JSON všech parametru
        var json = GSON.toJson(params);
        var jsonBytes = json.getBytes();
        // slouží jako uživatelské jméno
        var jsonBase64 = encode(jsonBytes, 0, jsonBytes.length, WEBSAFE_ALPHABET, false);
        // vygeneruje ověřovací parametr "pass"
        var pass = HashUtils.toHexMd5(json + json + HashUtils.toHexMd5(json));
        // vygeneruje absolutní URI
        var uri = getAbsoluteUri(opcode.getTarget(), jsonBase64, pass);

        HttpsURLConnection conn = null;
        Map responseMap;

        LOG.trace("Target URI: {}", uri);

        try {
            conn = createConnection(uri, getUserAgent(module), module.getBotnet().getConfig().getProxyData());
            var responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                String response;

                try (var baos = new ByteArrayOutputStream()) {
                    var is = new BufferedInputStream(conn.getInputStream());

                    for (var reader = is.read(); reader != -1; reader = is.read()) {
                        baos.write(reader);
                    }
                    response = baos.toString("UTF-8");
                    LOG.debug("Response for opcode '{}->{}' from URI '{}' is: {}", opcode.getTarget(), opcode
                            .getOpcodeValue(), uri, response);
                    responseMap = Json.toMap(response);

                    if (responseMap.containsKey("result")) {
                        var result = (String) responseMap.get("result");

                        // null může nastat a v takovém případě je to v pořádku
                        if (result != null) {
                            switch (result) {
                                case "2":
                                    throw new InvalidLoginException("2", "Invalid username or password");

                                case "10":
                                    throw new InvalidRequestException("10", "The server returned error 10, this might mean the robot is outdated, bug ocurred or invalid request");

                                case "1":
                                    throw new AccountBlockedException("1", "Your account with username '" + module
                                            .getBotnet().getConfig().getUserName() + "' " + "was blocked");

                                case "36":
                                    throw new InvalidAccessTokenException("36", "Token is invalid, you must sign in " + "again to get a new one");

                                case "0":
                                    LOG.debug("Returns 0 - OK");
                                    break;

                                default:
                                    LOG.debug("Returns {} - ???", result);
                                    break;
                            }
                        } else {
                            LOG.debug("Returns NULL - OK");
                        }
                    }
                }
            } else {
                throw new InvalidResponseCodeException(responseCode, "Failed to get response from vHackOS " + responseCode + " {" + uri + "}");
            }
        } catch (IOException e) {
            LOG.error("Something is wrong with processing the request. URI: " + uri, e);
            throw new ConnectionException(null, "An error occurred while processing request: " + uri, e);
        } finally {
            if (conn != null) {
                LOG.debug("Closing connection for opcode: {}->{}", opcode.getTarget(), opcode.getOpcodeValue());
                conn.disconnect();
            }
        }
        return responseMap;
    }

    /**
     * Vytvoří HTTPS připojení.
     */
    private HttpsURLConnection createConnection(String uri, String userAgent, ProxyData proxyData) throws IOException {
        var url = new URL(uri);
        HttpsURLConnection conn;

        if (proxyData != null) {
            var proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyData.getIp(), proxyData.getPort()));
            LOG.debug("A proxy with the address will be used: {}", proxy);
            conn = (HttpsURLConnection) url.openConnection(proxy);
        } else {
            conn = (HttpsURLConnection) url.openConnection();
        }
        conn.setRequestProperty("User-Agent", userAgent);
        conn.setRequestProperty("Accept-Encoding", "gzip");
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        return conn;
    }

    /**
     * Získá user-agent pro request.
     */
    private String getUserAgent(IModule module) {
        var connData = module.getBotnet().getConnectionData();
        var userAgent = connData.getUserAgent();

        if (StringUtils.isEmpty(userAgent)) {
            userAgent = UserAgentGenerator.generateNew();
            connData.setUserAgent(userAgent);
        }
        return userAgent;
    }

    /**
     * Vloží do requestu další parametry (např. o přihlášeném uživateli).
     */
    private synchronized void injectInitParams(Map<String, String> params, IModule module) {
        var connData = module.getBotnet().getConnectionData();

        if (connData.getUid() != null && connData.getUid() > 0) {
            params.put("uid", connData.getUid().toString());
        }
        if (StringUtils.isNotEmpty(connData.getAccessToken())) {
            params.put("accesstoken", connData.getAccessToken());
        }
        if (StringUtils.isNotEmpty(connData.getLang())) {
            params.put("lang", connData.getLang());
        }
        if (opcode.getOpcodeValue() != null) {
            params.put("action", String.valueOf(opcode.getOpcodeValue()));
        }
    }

    /**
     * Získá absolutní URI včetně parametrů.
     *
     * @param target   Cíl adresy na kterou se má komunikovat.
     * @param userData Data uživatele (v base64).
     * @param pass     Ověřovací hash v md5.
     */
    private static String getAbsoluteUri(OpcodeTargetType target, String userData, String pass) {
        return String.format("%s/%s.php?user=%s&pass=%s", REST_URI, target.getCode(), userData, pass);
    }

    /**
     * Metoda přebrána z vHackOS.<br>
     * Vytvoří base64.
     */
    private static String encode(byte[] source, int offset, int len, byte[] alphabet, boolean doPadding) {
        var outBuff = encode(source, offset, len, alphabet, Integer.MAX_VALUE);
        var outLen = outBuff.length;

        while (!doPadding && outLen > 0 && outBuff[outLen - 1] == EQUALS_SIGN) {
            outLen--;
        }
        return new String(outBuff, 0, outLen);
    }

    /**
     * Metoda přebrána z vHackOS.<br>
     * Vytvoří base64.
     */
    private static byte[] encode(byte[] source, int offset, int len, byte[] alphabet, int maxLineLength) {
        var len43 = ((len + 2) / 3) * 4;
        var outBuff = new byte[((len43 / maxLineLength) + len43)];
        var d = 0;
        var e = 0;
        var len2 = len - 2;
        var lineLength = 0;

        while (d < len2) {
            var inBuff = (((source[d + offset] << 24) >>> 8) | ((source[(d + 1) + offset] << 24) >>> 16)) | ((source[(d + 2) + offset] << 24) >>> 24);

            outBuff[e] = alphabet[inBuff >>> 18];
            outBuff[e + 1] = alphabet[(inBuff >>> 12) & 63];
            outBuff[e + 2] = alphabet[(inBuff >>> 6) & 63];
            outBuff[e + 3] = alphabet[inBuff & 63];
            lineLength += 4;

            if (lineLength == maxLineLength) {
                outBuff[e + 4] = (byte) 10;
                e++;
                lineLength = 0;
            }
            d += 3;
            e += 4;
        }
        if (d < len) {
            encode3to4(source, d + offset, len - d, outBuff, e, alphabet);

            if (lineLength + 4 == maxLineLength) {
                outBuff[e + 4] = (byte) 10;
                e++;
            }
            e += 4;
        }
        if (e == outBuff.length) {
            return outBuff;
        }
        throw new AssertionError();
    }

    /**
     * Metoda přebrána z vHackOS.<br>
     * Vytvoří base64.
     */
    private static byte[] encode3to4(byte[] src, int srcOffset, int numSigBytes, byte[] dest, int destOffset,
            byte[] alphabet) {

        int i;
        var i2 = 0;

        if (numSigBytes > 0) {
            i = (src[srcOffset] << 24) >>> 8;
        } else {
            i = 0;
        }
        var i3 = (numSigBytes > 1 ? (src[srcOffset + 1] << 24) >>> 16 : 0) | i;

        if (numSigBytes > 2) {
            i2 = (src[srcOffset + 2] << 24) >>> 24;
        }
        var inBuff = i3 | i2;

        switch (numSigBytes) {
            case 1:
                dest[destOffset] = alphabet[inBuff >>> 18];
                dest[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                dest[destOffset + 2] = EQUALS_SIGN;
                dest[destOffset + 3] = EQUALS_SIGN;
                break;
            case 2:
                dest[destOffset] = alphabet[inBuff >>> 18];
                dest[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                dest[destOffset + 2] = alphabet[(inBuff >>> 6) & 63];
                dest[destOffset + 3] = EQUALS_SIGN;
                break;
            case 3:
                dest[destOffset] = alphabet[inBuff >>> 18];
                dest[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                dest[destOffset + 2] = alphabet[(inBuff >>> 6) & 63];
                dest[destOffset + 3] = alphabet[inBuff & 63];
                break;
        }
        return dest;
    }
}
