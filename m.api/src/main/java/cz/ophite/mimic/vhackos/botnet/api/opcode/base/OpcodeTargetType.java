package cz.ophite.mimic.vhackos.botnet.api.opcode.base;

/**
 * Cíl na který opcode bude směřovat. Kód reprezentuje název PHP souboru.
 *
 * @author mimic
 */
public enum OpcodeTargetType {
    /**
     * Přihlášení uživatele.
     */
    LOGIN("login"),
    /**
     * Registrace uživatele.
     */
    REGISTER("register"),
    /**
     * Informace o uživateli a aktualizuje token.
     */
    UPDATE("update"),
    /**
     * Informace o SDK uživatele.
     */
    SDK("sdk"),
    /**
     * Skenuje IP adresy.
     */
    NETWORK("network"),
    /**
     * Obsah poznámkového bloku.
     */
    NOTEPAD("notepad"),
    /**
     * Hackuje IP adresy.
     */
    EXPLOIT("exploit"),
    /**
     * Ovládá log na cílové IP.
     */
    REMOTE_LOG("remotelog"),
    /**
     * Zahájí prolamování banky.
     */
    START_BRUTEFORCE("startbruteforce"),
    /**
     * Ovládá vzdálenou banku.
     */
    REMOTE_BANKING("remotebanking"),
    /**
     * Informace o cílové IP.
     */
    REMOTE("remote"),
    /**
     * Log uživatele.
     */
    LOG("log"),
    /**
     * Práce s malware kitem.
     */
    MALWARE_KIT("mwk"),
    /**
     * Informace o táscích.
     */
    TASKS("tasks"),
    /**
     * Informace z vlastní banky.
     */
    BANKING("banking"),
    /**
     * Obchod aplikací.
     */
    STORE("store"),
    /**
     * Informace o profilu uživatele.
     */
    PROFILE("profile"),
    /**
     * Nahrání vlastního pozadí.
     */
    UPLOAD_BACKGROUND("uploadbg"),
    /**
     * Smaže pozadí.
     */
    CLEAR_BACKGROUND("clearbg"),
    /**
     * Nákupy za peníze nebo netcoins.
     */
    BUY("buy"),
    /**
     * Správa netcoin mineru.
     */
    MINING("mining"),
    /**
     * Správa serveru.
     */
    SERVER("server"),
    /**
     * Leaderboards.
     */
    RANKING("ranking"),
    /**
     * Správa misí.
     */
    MISSIONS("missions"),
    /**
     * Správa crew.
     */
    CREW("crew"),
    /**
     * Profil crew.
     */
    CREW_PROFILE("crewprofile"),
    /**
     * Umožní smazat účet uživatele.
     */
    DELETE_ACCOUNT("deleteAccount");

    private final String code; // název php souboru

    OpcodeTargetType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
