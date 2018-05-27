package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.BankTransactionData;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMoneyConverter;

import java.util.List;

/**
 * Informace o vlastní bance.
 *
 * @author mimic
 */
public final class BankResponse extends Response {

    @AsciiRow(value = "Filled", converter = AsciiBooleanConverter.class)
    @ResponseKey("filled")
    private Integer filled;
    public static final String P_FILLED = "filled";

    @AsciiRow(value = "Money", converter = AsciiMoneyConverter.class)
    @ResponseKey("money")
    private Long money;
    public static final String P_MONEY = "money";

    @AsciiRow(value = "Savings", converter = AsciiMoneyConverter.class)
    @ResponseKey("savings")
    private Long savings;
    public static final String P_SAVINGS = "savings";

    @AsciiRow("User")
    @ResponseKey("username")
    private String userName;
    public static final String P_USER_NAME = "userName";

    @AsciiRow(value = "Total", converter = AsciiMoneyConverter.class)
    @ResponseKey(KEY_TOTAL)
    private Long total;
    public static final String P_TOTAL = "total";
    public static final String KEY_TOTAL = "total";

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

    public Integer getFilled() {
        return filled;
    }

    public void setFilled(Integer filled) {
        this.filled = filled;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getSavings() {
        return savings;
    }

    public void setSavings(Long savings) {
        this.savings = savings;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
