package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiDateConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMoneyConverter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Informace o transakci v bance.
 *
 * @author mimic
 */
public final class BankTransactionData {

    @AsciiRow(value = "Time", converter = AsciiDateConverter.class)
    @ResponseKey(KEY_TIME)
    private Long time;
    public static final String P_TIME = "time";
    private static final String KEY_TIME = "time";

    @AsciiRow("From ID")
    @ResponseKey(KEY_FROM_ID)
    private Integer fromId;
    public static final String P_FROM_ID = "fromId";
    private static final String KEY_FROM_ID = "from_id";

    @AsciiRow("From IP")
    @ResponseKey(KEY_FROM_IP)
    private String fromIp;
    public static final String P_FROM_IP = "fromIp";
    private static final String KEY_FROM_IP = "from_ip";

    @AsciiRow("To ID")
    @ResponseKey(KEY_TO_ID)
    private Integer toId;
    public static final String P_TO_ID = "toId";
    private static final String KEY_TO_ID = "to_id";

    @AsciiRow("To IP")
    @ResponseKey(KEY_TO_IP)
    private String toIp;
    public static final String P_TO_IP = "toIp";
    private static final String KEY_TO_IP = "to_ip";

    @AsciiRow(value = "Amount", converter = AsciiMoneyConverter.class)
    @ResponseKey(KEY_AMOUNT)
    private Long amount;
    public static final String P_AMOUNT = "amount";
    private static final String KEY_AMOUNT = "amount";

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getToIp() {
        return toIp;
    }

    public void setToIp(String toIp) {
        this.toIp = toIp;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("time", time).append("fromId", fromId).append("fromIp", fromIp)
                .append("toId", toId).append("toIp", toIp).append("amount", amount).toString();
    }
}
