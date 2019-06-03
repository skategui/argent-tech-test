package agis.guillaume.argent.repo;

import agis.guillaume.argent.BuildConfig;
import agis.guillaume.argent.api.EtherScanResource;
import agis.guillaume.argent.models.requests.BalanceResponse;
import agis.guillaume.argent.models.requests.ERC20TokenResponse;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Responsible to make HTTP requests in order to load the data of the account
 */
public class AccountRepositoryImpl implements AccountRepository {

    private final EtherScanResource resource;


    /**
     * Default constructor, the params are provided by dagger
     *
     * @param resource Retrofit resource  associated to the Etherscan.io api
     */
    @Inject
    AccountRepositoryImpl(EtherScanResource resource) {
        this.resource = resource;
    }

    /**
     * get the balance, in ETH, associated to the account provided in BuildConfig.ACCOUNT_ADDR
     *
     * @return [Single] [BigDecimal] balance in ETH
     */
    @Override
    public Single<BigDecimal> getETHBalance() {
        return resource.getETHBalance(BuildConfig.ACCOUNT_ADDR, BuildConfig.ETHERSCAN_API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(BalanceResponse::getResult)
                .map(BigDecimal::new);
    }

    /**
     * Get the ERC 20 tokens  associated to the account provided in BuildConfig.ACCOUNT_ADDR
     *
     * @return [Single] [ERC20TokenResponse] erc 20 tokens
     */
    @Override
    public Single<ERC20TokenResponse> getERC20Tokens() {
        return resource.getERC20(BuildConfig.ACCOUNT_ADDR, BuildConfig.ETHERSCAN_API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }
}
