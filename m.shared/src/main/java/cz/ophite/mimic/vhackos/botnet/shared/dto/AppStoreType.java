package cz.ophite.mimic.vhackos.botnet.shared.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Typ aplikace v obchodu.
 *
 * @author mimic
 */
public enum AppStoreType {

    NOTEPAD(0, false, "NOTE", "Notepad"),
    ANTIVIRUS(1, true, "AV", "Antivirus"),
    FIREWALL(2, true, "FW", "Firewall"),
    SPAM(3, true, "SP", "Spam"),
    BRUTEFORCE(4, true, "BF", "Bruteforce"),
    BANKING_PROTECTION(5, true, "BP", "Banking Protection"),
    SOFTWARE_DEV_KIT(6, true, "SDK", "Software Development Kit"),
    COMMUNITY(7, false, "COMMUNITY", "Community"),
    MISSIONS(8, false, "MISSIONS", "Missions"),
    LEADERBOARDS(9, false, "LEADER", "Leaderboards"),
    IPS(10, true, "IPS", "IP-Spoofing"),
    NC_MINER(11, false, "NCM", "ncMiner"),
    CREW(12, false, "CREW", "Crew"),
    SERVER(13, false, "SERVER", "Server"),
    MALWARE_KIT(14, false, "MWK", "Malware Kit"),
    JOBS(15, false, "JOBS", "Jobs");

    public static final List<String> UPDATABLE_APP_CODES = AppStoreType.getUpdatableAppCodes();

    private final int id;
    private final String alias;
    private final String code;
    private final boolean updatable;

    AppStoreType(int id, boolean updatable, String code, String alias) {
        this.id = id;
        this.updatable = updatable;
        this.code = code;
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public String getCode() {
        return code;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public static AppStoreType getById(int appId) {
        for (var type : values()) {
            if (type.id == appId) {
                return type;
            }
        }
        return null;
    }

    public static AppStoreType getByCode(String code) {
        for (var type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    private static List<String> getUpdatableAppCodes() {
        var list = new ArrayList<String>();
        for (var type : values()) {
            if (type.updatable) {
                list.add(type.getCode());
            }
        }
        return list;
    }
}
