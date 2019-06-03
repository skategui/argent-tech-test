package agis.guillaume.argent.usecase;

import agis.guillaume.argent.models.ERC20TokenUser;
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
     * Check if the erc 20 tokens list in memory if empty or not to check if the
     * data needs to be re-loaded from the server
     *
     * @return [Single] [List] [ERC20TokenUser] erc 20 tokens list
     */
    Single<List<ERC20TokenUser>> getERC20Tokens();

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
