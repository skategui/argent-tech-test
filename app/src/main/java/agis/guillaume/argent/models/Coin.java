package agis.guillaume.argent.models;

import java.math.BigDecimal;

public final class Coin {

    private final String name;
    private final String symbol;
    private final BigDecimal amount;
    private final BigDecimal balance;

    public Coin(String name, String symbol, BigDecimal amount, BigDecimal balance) {
        this.name = name;
        this.symbol = symbol;
        this.amount = amount;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Check if the pair is available for trading. If it's not possible, then the value is negative, it means
     * we could not determine if the balance in ETH for this coin
     * @return true if pair available for trading or not
     */
    public boolean isAvailableForTrading() {
        return balance.doubleValue() >= 0.0;
    }


}