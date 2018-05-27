package cz.ophite.mimic.vhackos.botnet.api.module.base;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.IBotnetConfig;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.ConnectionException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidAccessTokenException;
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

            int attempts = getBotnet().getConfig().getMaxRequestAttempts(); // maximální počet pokusů

            while (attempts > 0) {
                try {
                    Thread.sleep(getConfig().getSleepDelay());
                    var request = new OpcodeRequest(opcode);
                    return request.send(this);

                } catch (InvalidAccessTokenException e) {
                    try {
                        log.info("Invalid access token. Getting a new...");
                        Thread.sleep(5000);
                        try {
                            var loginData = commonModule.login();
                            log.info("User '{}' was re-logged in", loginData.getUserName());
                            getBotnet().reloginCallback(loginData);
                            Thread.sleep(3000);

                        } catch (BotnetException be) {
                            attempts--;
                            log.error("Login failed. Remaining attempts: {}", attempts);
                        }
                    } catch (InterruptedException e1) {
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
            throw new ConnectionException("Could not send request to server");
        }
    }
}
