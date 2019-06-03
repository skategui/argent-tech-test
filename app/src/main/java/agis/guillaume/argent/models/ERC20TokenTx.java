package agis.guillaume.argent.models;

import agis.guillaume.argent.BuildConfig;
import com.google.gson.annotations.SerializedName;

public class ERC20TokenTx {

    @SerializedName("value")
    private String balance;

    @SerializedName("tokenSymbol")
    private String tokenSymbol;

    @SerializedName("tokenName")
    private String tokenName;


    @SerializedName("from")
    private String transactionFrom;

    @SerializedName("to")
    private String transactionTo;

    public String getTransactionFrom() {
        return transactionFrom;
    }

    public void setTransactionFrom(String transactionFrom) {
        this.transactionFrom = transactionFrom;
    }

    public String getTransactionTo() {
        return transactionTo;
    }

    public void setTransactionTo(String transactionTo) {
        this.transactionTo = transactionTo;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * Check if the transaction has been made by the current account, to know if we should add or remove from the balance
     * @return true if the tx has been made by this account
     */
    public boolean isTxMadeByUser() {
        return BuildConfig.ACCOUNT_ADDR.toLowerCase().equals(transactionFrom.toLowerCase());
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String token) {
        this.balance = token;
    }
}
