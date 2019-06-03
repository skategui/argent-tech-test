package agis.guillaume.argent.models;

import agis.guillaume.argent.utils.WeiUtils;

import java.math.BigDecimal;

// used to store the tokens owned by the user and their balance
public final class TokenWithBalance {

    private final String name;
    private final String symbol;
    private BigDecimal amount;

    public TokenWithBalance(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
        amount = new BigDecimal(0);
    }

    public void addToken(BigDecimal value)
    {
        amount =  amount.add(value);
    }
    public void removeToken(BigDecimal value)
    {
        amount =  amount.subtract(value);
    }



    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getAmount() {
        return WeiUtils.convertWeiToETH(amount);
    }

}