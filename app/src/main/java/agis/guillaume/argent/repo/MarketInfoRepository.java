package agis.guillaume.argent.repo;

import agis.guillaume.argent.models.requests.MarketInfo;
import io.reactivex.Single;

import java.util.List;

public interface MarketInfoRepository {

    /**
     * Get the list of available pairs from the server, format : BTC-ETH, in uppercase
     *
     * @return [Single] [List] [String] List of pairs available
     */
    Single<List<String>> validPairs();

    /**
     * Get the market info given a pair of tokens
     *
     * @param pair pair to get the market info
     * @return [Single] [MarketInfo] market info associated to this pair
     */
    Single<MarketInfo> marketInfo(String pair);
}
