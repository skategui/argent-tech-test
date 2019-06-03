package agis.guillaume.argent.usecase;

import agis.guillaume.argent.models.Coin;
import agis.guillaume.argent.models.ERC20TokenTx;
import agis.guillaume.argent.models.TokenWithBalance;
import agis.guillaume.argent.models.requests.ERC20TokenResponse;
import agis.guillaume.argent.repo.AccountRepository;
import agis.guillaume.argent.storage.profile.AccountDataStore;
import agis.guillaume.argent.utils.WeiUtils;
import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

/**
 * Responsible for the business logic associated to the account.
 */
public class AccountUseCaseImpl implements AccountUseCase {


    private final AccountRepository accountRepository;
    private final AccountDataStore accountDataStore;
    private final TokenUseCase tokenUseCase;

    private List<Coin> coins = new ArrayList<>();


    /**
     * Default constructor, the params are provided by dagger
     *
     * @param tokenUseCase      Responsible for the Token business logic
     * @param accountRepository Repository to get the data of the account remotely
     * @param accountDataStore  datastore to store locally the data associated to the account
     */
    @Inject
    AccountUseCaseImpl(TokenUseCase tokenUseCase,
                       AccountRepository accountRepository,
                       AccountDataStore accountDataStore) {
        this.accountRepository = accountRepository;
        this.tokenUseCase = tokenUseCase;
        this.accountDataStore = accountDataStore;
    }


    /**
     * get the balance, in ETH, associated to this account
     *
     * @return [Single] [BigDecimal] balance in ETH
     */
    @Override
    public Single<BigDecimal> getETHBalance() {
        return getETHBalanceRemotely();
    }

    /**
     * get the balance, in ETH, associated to this account from the server
     *
     * @return [Single] [BigDecimal] balance in ETH
     */
    private Single<BigDecimal> getETHBalanceRemotely() {
        return accountRepository.getETHBalance()
                .map(WeiUtils::convertWeiToETH)
                .doOnSuccess(this::storeLocallyEthBalance);
    }


    /**
     * Get the list of coins for this account, from the server
     *
     * @return [Single] [List] [Coin] list of coins owned by the user
     */
    @Override
    public Single<List<Coin>> loadOwnedCoins() {
        return coins.isEmpty() ? getERC20TokensRemotely() : Single.just(coins);
    }


    /**
     * Get the balance in ETH associated to the user that has been stored locally
     *
     * @return [BigDecimal] balance stored
     */
    @Override
    public BigDecimal getETHBalanceLocally() {
        return accountDataStore.getEthBalance();
    }

    /**
     * Get the aggregation of all the ERC 20 tokens that has been stored locally
     *
     * @return [double] balance stored
     */
    @Override
    public BigDecimal getTotalERC20TokenBalanceLocally() {

        List<Coin> tokens = accountDataStore.getERC20TokenBalance();
        if (tokens == null || tokens.isEmpty())
            return new BigDecimal(0);
        storeLocallyERC20Token(tokens);
        return aggregateBalance(tokens);
    }

    /**
     * Sum up the balance for each ERC tokens.
     * Verify if the transaction is made from the current ADDR to know if the value found should be
     * added or removed.
     *
     * @param tokens list of tokens associated to this account
     * @return [double] aggregation of the balances.
     */
    private BigDecimal aggregateBalance(List<Coin> tokens) {
        BigDecimal value = new BigDecimal(0.0);

        for (Coin token : tokens) {
            if (token.isAvailableForTrading()) {
                value = value.add(token.getBalance());
            }
        }
        return value;
    }

    /**
     * Get the aggregation of the balance of all the ERC 20 tokens for that account
     *
     * @return [Single] [BigDecimal] balance in ETH of the ERC 20 tokens
     */
    @Override
    public Single<BigDecimal> getTotalERC20TokenBalance() {
        return loadOwnedCoins()
                .toObservable()
                .flatMapIterable(it -> it)
                .filter(Coin::isAvailableForTrading)
                .map(Coin::getBalance)
                .reduce(BigDecimal::add)
                .toSingle();

    }


    /**
     * Take the list of ERc 20 tokens from the current Addr and then transform it into a list of
     * tokens associated to the ERC 20 with the balance.
     *
     * @param erc20TokenTxList erc 20 tokens
     * @return created list of tokens with the balance
     */
    private Single<List<Coin>> loadERC20TokenInformations(List<ERC20TokenTx> erc20TokenTxList) {
        return tokenUseCase.validPairs()
                .flatMap(it -> getBalancesByToken(erc20TokenTxList))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMapIterable(it -> it)
                .filter(it -> tokenUseCase.isValidPairEth(it.getSymbol()))
                .flatMap(coin -> tokenUseCase.getBalance(
                        coin.getSymbol(),
                        coin.getAmount()
                        ).map(balance -> createCoin(coin.getName(), coin.getSymbol(), coin.getAmount(), balance))
                                .toObservable()

                )
                .toList()
                .doOnSuccess(accountDataStore::setERC20TokensBalance);
    }


    /**
     * Create a list of transactions mapping the token and its balance
     *
     * @param erc20TokenTxList list of ERC 20 tokens from the current user
     * @return [Single] [Collection]  [TokenWithBalance]  list created
     */
    private Single<Collection<TokenWithBalance>> getBalancesByToken(List<ERC20TokenTx> erc20TokenTxList) {
        Map<String, TokenWithBalance> balances = new HashMap<>();
        for (ERC20TokenTx currentTx : erc20TokenTxList) {

            BigDecimal balance = new BigDecimal(currentTx.getBalance());
            String symbol = currentTx.getTokenSymbol();

            TokenWithBalance currentBalance = balances.get(symbol);

            if (currentBalance == null) {
                currentBalance = new TokenWithBalance(currentTx.getTokenName(), currentTx.getTokenSymbol());
            }
            if (currentTx.isTxMadeByUser()) {
                currentBalance.removeToken(balance);
            } else
                currentBalance.addToken(balance);

            balances.put(symbol, currentBalance);
        }
        return Single.just(balances.values());
    }


    private Coin createCoin(@NonNull String name,
                            @NonNull String symbol,
                            BigDecimal tokenNumber,
                            BigDecimal balance) {
        return new Coin(name, symbol, tokenNumber, balance);
    }


    /**
     * Get the ERC 20 tokens list from the server
     *
     * @return [Single] [List] [Coin] erc 20 tokens list
     */

    private Single<List<Coin>> getERC20TokensRemotely() {
        return accountRepository.getERC20Tokens()
                .map(ERC20TokenResponse::getResult)
                .flatMap(this::loadERC20TokenInformations)
                .doOnSuccess(this::storeLocallyERC20Token);
    }

    /**
     * Store the ETh balance locally
     *
     * @param balance balance to store locally
     */
    private void storeLocallyEthBalance(BigDecimal balance) {
        accountDataStore.setBalanceEth(balance);
    }

    /**
     * Store the list of coins owned by the user locally
     *
     * @param coins Coins to store to locally
     */
    private void storeLocallyERC20Token(@NonNull List<Coin> coins) {
        accountDataStore.setERC20TokensBalance(coins);
        this.coins.clear();
        this.coins.addAll(coins);
    }


}
