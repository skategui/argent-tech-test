package agis.guillaume.argent.usecase;

import androidx.annotation.NonNull;
import io.reactivex.Single;

import java.math.BigDecimal;
import java.util.List;

public interface TokenUseCase {

    /**
     * Verify if this pair is a valid pair to trade
     *
     * @param symbol symbol to trade against ETH
     * @return true if it's a valid pair, false otherwise
     */
    boolean isValidPairEth(String symbol);

    /**
     * Get the list of available pairs, format : BTC_ETH, in uppercase
     *
     * @return [Single] [List] [String] List of pairs available of the market
     */
    Single<List<String>> validPairs();


    /**
     * Get the total balance for a coin, associated by it's symbol, in ETH.
     *
     * @param symbol Symbol associated to the pair to get the market info
     * @param amount amount of coins associated to this token
     * @return [Single] [Double] rate found
     */
    Single<BigDecimal> getBalance(@NonNull String symbol, BigDecimal amount);
}
