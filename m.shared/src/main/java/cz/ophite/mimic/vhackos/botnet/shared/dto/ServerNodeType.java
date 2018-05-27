package cz.ophite.mimic.vhackos.botnet.shared.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Typ nodu na serveru.
 *
 * @author mimic
 */
public enum ServerNodeType {

    SERVER(0, "srv", "Server"),
    ANTIVIRUS(1, "av", "Antivirus"),
    FIREWALL(2, "fw", "Firewall");

    private final String alias;
    private final int code;
    private final String command;

    ServerNodeType(int code, String command, String alias) {
        this.code = code;
        this.command = command;
        this.alias = alias;
    }

    public int getCode() {
        return code;
    }

    public String getAlias() {
        return alias;
    }

    public static ServerNodeType getByCommand(String command) {
        for (var type : values()) {
            if (type.command.equalsIgnoreCase(command)) {
                return type;
            }
        }
        return null;
    }

    public static ServerNodeType getByCode(int code) {
        for (var type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getCommands() {
        var list = new ArrayList<String>();
        for (var type : values()) {
            list.add(type.command);
        }
        return list;
    }
}
