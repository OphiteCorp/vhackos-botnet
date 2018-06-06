package cz.ophite.mimic.vhackos.botnet.api.module.base;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.IBotnetConfig;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.ConnectionException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidAccessTokenException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidResponseCodeException;
import cz.ophite.mimic.vhackos.botnet.api.module.CommonModule;
import cz.ophite.mimic.vhackos.botnet.api.net.OpcodeRequest;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.IOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Základní implementace modulu.
 *
 * @author mimic
 */
public abstract class Module implements IModule {

    private static final Object LOCK = new Object();

    private final Logger log;
    private final IBotnet botnet;

    @Autowired
    private CommonModule commonModule;

    protected Module(IBotnet botnet) {
        this.botnet = botnet;
        log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public final IBotnet getBotnet() {
        return botnet;
    }

    protected final IBotnetConfig getConfig() {
        return botnet.getConfig();
    }

    protected final Logger getLogger() {
        return log;
    }

    protected final CommonModule getCommonModule() {
        return commonModule;
    }

    /**
     * Získá hodnotu RESULT.
     */
    protected final String getResultValue(Map<String, Object> response) {
        if (response.containsKey("result")) {
            var result = response.get("result");
            return (result != null) ? result.toString() : (String) result;
        }
        return "";
    }

    /**
     * Odešle požadavek na server.
     */
    protected final Map<String, Object> sendRequest(IOpcode opcode) {
        synchronized (LOCK) {
            log.debug("Sending request '{}->{}' for userName: {}", opcode.getTarget(), opcode
                    .getOpcodeValue(), getConfig().getUserName());

            int maxAttempts = getBotnet().getConfig().getMaxRequestAttempts();
            int attempts = getBotnet().getConfig().getMaxRequestAttempts();
            Exception prevException = null;

            while (attempts > 0) {
                try {
                    Thread.sleep(getConfig().getSleepDelay());
                    var request = new OpcodeRequest(opcode);
                    var response = request.send(this);

                    if (attempts != maxAttempts) {
                        log.info("The request to the server was finally sent");
                    }
                    return response;

                } catch (InvalidAccessTokenException e) {
                    prevException = e;
                    try {
                        log.info("Invalid access token. Getting a new...");
                        if (!getConfig().isAggressiveMode()) {
                            Thread.sleep(5000);
                        }
                        try {
                            var loginData = commonModule.login();
                            log.info("User '{}' was re-logged in", loginData.getUserName());
                            getBotnet().reloginCallback(loginData);

                            if (!getConfig().isAggressiveMode()) {
                                Thread.sleep(3000);
                            }
                        } catch (BotnetException be) {
                            attempts--;
                            log.error("Login failed. Remaining attempts: {}/{}", attempts, maxAttempts);
                        }
                    } catch (InterruptedException e1) {
                        break;
                    }
                } catch (ConnectionException e) {
                    prevException = e;
                    attempts--;
                    log.error("Request failed. Probably failed to establish communication with the server. Remaining attempts: {}/{}", attempts, maxAttempts);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e1) {
                        break;
                    }
                } catch (InvalidResponseCodeException e) {
                    if (e.getResponseCode() == 404) {
                        prevException = e;
                        attempts--;
                        log.warn("The server is busy. Remaining attempts: {}/{}", attempts, maxAttempts);
                        try {
                            Thread.sleep(getConfig().isAggressiveMode() ? 5000 : 10000);
                        } catch (InterruptedException e1) {
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
            throw new ConnectionException(null, "Could not send request to server", prevException);
        }
    }
}
