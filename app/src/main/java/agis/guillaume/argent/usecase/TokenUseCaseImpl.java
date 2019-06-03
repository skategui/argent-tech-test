package agis.guillaume.argent.usecase;

import agis.guillaume.argent.repo.MarketInfoRepository;
import agis.guillaume.argent.storage.token.MarketDataStore;
import agis.guillaume.argent.utils.WeiUtils;
import androidx.annotation.NonNull;
import io.reactivex.Single;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * Responsible for the business logic around the tokens and market infos
 */
public class TokenUseCaseImpl implements TokenUseCase {

    private static final String SUFFIX_PAIR_ETH = "_ETH";

    private final MarketInfoRepository marketInfoRepository;
    private final MarketDataStore marketDataStore;

    /**
     * Default constructor
     *
     * @param marketInfoRepository responsible for HTTP requests related to the tokens
     * @param marketDataStore      responsible for storing the info related to the market data locally
     */
    @Inject
    public TokenUseCaseImpl(MarketInfoRepository marketInfoRepository,
                            MarketDataStore marketDataStore) {
        this.marketInfoRepository = marketInfoRepository;
        this.marketDataStore = marketDataStore;
    }


    /**
     * Verify if this pair is a valid pair to trade with ETH
     *
     * @param symbol symbol to trade against ETH
     * @return true if it's a valid pair, false otherwise
     */
    @Override
    public boolean isValidPairEth(String symbol) {
        return marketDataStore.isValidPair(symbol.toUpperCase() + SUFFIX_PAIR_ETH);
    }


    /**
     * Get the list of available pairs, format : BTC-ETH, in uppercase
     *
     * @return [Single] [List] [String] List of pairs available
     */
    @Override
    public Single<List<String>> validPairs() {
        List<String> validPairs = marketDataStore.getValidPairs();
        return validPairs.isEmpty() ?
                marketInfoRepository.validPairs().doOnSuccess(marketDataStore::storePairs)
                : Single.just(validPairs);
    }

    /**
     * Get the total balance for a coin, associated by it's symbol, in ETH.
     * Return -1 if there is an error from the server, such as not pair not available for trading.
     * @param symbol Symbol associated to the pair to get the market info
     * @param amount amount of coins associated to this token
     * @return [Single] [Double] rate found, -1 if there was an error loading the rate.
     */
    @Override
    public Single<BigDecimal> getBalance(@NonNull String symbol, BigDecimal amount) {
        if (amount.doubleValue() == 0.0)
            return Single.just(new BigDecimal(0.0));
        return marketInfoRepository.marketInfo(symbol.toUpperCase() + SUFFIX_PAIR_ETH)
                .map(it -> amount.multiply(new BigDecimal(it.getRate())))
                .onErrorResumeNext(it -> Single.just(new BigDecimal(-1)));
    }

}
