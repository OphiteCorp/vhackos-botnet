package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.BankTransactionData;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiMoneyConverter;

import java.util.List;

/**
 * Informace o bankovním účtu cílového systému.
 *
 * @author mimic
 */
public final class RemoteBankResponse extends Response {

    @AsciiRow(value = "Withdraw", converter = AsciiBooleanConverter.class)
    @ResponseKey("withdraw")
    private Integer withdraw;
    public static final String P_WITHDRAW = "withdraw";

    @AsciiRow(value = "Open", converter = AsciiBooleanConverter.class)
    @ResponseKey("open")
    private Integer open;
    public static final String P_OPEN = "open";

    @AsciiRow(value = "IP Removed", converter = AsciiBooleanConverter.class)
    @ResponseKey("ipremoved")
    private Integer ipRemoved;
    public static final String P_IP_REMOVED = "ipRemoved";

    @AsciiRow("Target ID")
    @ResponseKey("target_id")
    private Integer targetId;
    public static final String P_TARGET_ID = "targetId";

    @AsciiRow("Remote User")
    @ResponseKey("remoteusername")
    private String remoteUserName;
    public static final String P_REMOTE_USER_NAME = "remoteUserName";

    @AsciiRow("User Name")
    @ResponseKey("username")
    private String userName;
    public static final String P_USER_NAME = "userName";

    @AsciiRow("Remote Password")
    @ResponseKey("remotepassword")
    private String remotePassword;
    public static final String P_REMOTE_PASSWORD = "remotePassword";

    @AsciiRow(value = "Remote Money", converter = AsciiMoneyConverter.class)
    @ResponseKey("remotemoney")
    private Long remoteMoney;
    public static final String P_REMOTE_MONEY = "remoteMoney";

    @AsciiRow(value = "Money", converter = AsciiMoneyConverter.class)
    @ResponseKey("money")
    private Long money;
    public static final String P_MONEY = "money";

    @AsciiRow(value = "Savings", converter = AsciiMoneyConverter.class)
    @ResponseKey("savings")
    private Long savings;
    public static final String P_SAVINGS = "savings";

    @AsciiRow("User Malware Kits")
    @ResponseKey("usrMwk")
    private Integer userMalwareKits;
    public static final String P_USER_MALWARE_KITS = "userMalwareKits";

    @AsciiRow(value = "Got BLT", converter = AsciiBooleanConverter.class)
    @ResponseKey("gotBLT")
    private Integer gotBLT;
    public static final String P_GOT_BLT = "gotBLT";

    @AsciiRow("AATT")
    @ResponseKey("aatt")
    private Integer aatt;
    public static final String P_AATT = "aatt";

    @AsciiRow("Next P")
    @ResponseKey("nextp")
    private Integer nextp;
    public static final String P_NEXT_P = "nextp";

    @AsciiRow(value = "Total", converter = AsciiMoneyConverter.class)
    @ResponseKey("total")
    private Long total;
    public static final String P_TOTAL = "total";

    @AsciiRow(value = "Max Savings", converter = AsciiMoneyConverter.class)
    @ResponseKey("maxsavings")
    private Long maxSavings;
    public static final String P_MAX_SAVINGS = "maxSavings";

    @AsciiRow("Transactions Count")
    @ResponseKey("transcount")
    private Integer transactionsCount;
    public static final String P_TRANSACTIONS_COUNT = "transactionsCount";

    @AsciiRow("Transactions")
    @ResponseKey("transactions")
    private List<BankTransactionData> transactions;
    public static final String P_TRANSACTIONS = "transactions";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Integer withdraw) {
        this.withdraw = withdraw;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getIpRemoved() {
        return ipRemoved;
    }

    public void setIpRemoved(Integer ipRemoved) {
        this.ipRemoved = ipRemoved;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getRemoteUserName() {
        return remoteUserName;
    }

    public void setRemoteUserName(String remoteUserName) {
        this.remoteUserName = remoteUserName;
    }

    public String getRemotePassword() {
        return remotePassword;
    }

    public void setRemotePassword(String remotePassword) {
        this.remotePassword = remotePassword;
    }

    public Long getRemoteMoney() {
        return remoteMoney;
    }

    public void setRemoteMoney(Long remoteMoney) {
        this.remoteMoney = remoteMoney;
    }

    public Long getSavings() {
        return savings;
    }

    public void setSavings(Long savings) {
        this.savings = savings;
    }

    public Integer getUserMalwareKits() {
        return userMalwareKits;
    }

    public void setUserMalwareKits(Integer userMalwareKits) {
        this.userMalwareKits = userMalwareKits;
    }

    public Integer getGotBLT() {
        return gotBLT;
    }

    public void setGotBLT(Integer gotBLT) {
        this.gotBLT = gotBLT;
    }

    public Integer getAatt() {
        return aatt;
    }

    public void setAatt(Integer aatt) {
        this.aatt = aatt;
    }

    public Integer getNextp() {
        return nextp;
    }

    public void setNextp(Integer nextp) {
        this.nextp = nextp;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getMaxSavings() {
        return maxSavings;
    }

    public void setMaxSavings(Long maxSavings) {
        this.maxSavings = maxSavings;
    }

    public Integer getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(Integer transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public List<BankTransactionData> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BankTransactionData> transactions) {
        this.transactions = transactions;
    }
}
