package agis.guillaume.argent.usecase;

import agis.guillaume.argent.models.Coin;
import io.reactivex.Single;

import java.math.BigDecimal;
import java.util.List;

public interface AccountUseCase {
    /**
     * get the balance, in ETH, associated to this account
     *
     * @return [Single] [BigDecimal] balance in ETH
     */
    Single<BigDecimal> getETHBalance();

    /**
     *Get the aggregation of the balance of all the ERC 20 tokens for that account
     *
     * @return [Single] [BigDecimal] balance in ETH of the ERC 20 tokens
     */
    Single<BigDecimal> getTotalERC20TokenBalance();

    /**
     * Get the list of coins owned by this account
     *
     * @return [Single] [List] [Coin] coins owned by the user
     */
    Single<List<Coin>> loadOwnedCoins();

    /**
     * Get the balance in ETH associated to the user that has been stored locally
     *
     * @return [BigDecimal] balance stored
     */
    BigDecimal getETHBalanceLocally();

    /**
     * Get the aggregation of the balance of all the ERC tokens that has been stored locally
     *
     * @return [BigDecimal] balance stored
     */
    BigDecimal getTotalERC20TokenBalanceLocally();

}
