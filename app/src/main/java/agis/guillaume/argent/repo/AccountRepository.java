package agis.guillaume.argent.repo;

import agis.guillaume.argent.models.requests.ERC20TokenResponse;
import io.reactivex.Single;

import java.math.BigDecimal;

public interface AccountRepository {

    /**
     * get the balance, in ETH, associated to the account provided in BuildConfig.ACCOUNT_ADDR
     *
     * @return [Single] [BigDecimal] balance in ETH
     */
    Single<BigDecimal> getETHBalance();

    /**
     * Get the ERC 20 tokens transactions associated to the account provided in BuildConfig.ACCOUNT_ADDR
     *
     * @return [Single] [ERC20TokenResponse] erc 20 tokens
     */
    Single<ERC20TokenResponse> getERC20Tokens();
}
