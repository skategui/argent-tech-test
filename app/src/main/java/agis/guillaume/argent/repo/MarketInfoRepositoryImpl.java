package agis.guillaume.argent.repo;

import agis.guillaume.argent.api.ShapeShiftResource;
import agis.guillaume.argent.models.requests.CoinResponse;
import agis.guillaume.argent.models.requests.MarketInfo;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Responsible to make HTTP requests in order to load the Market infos
 */
public class MarketInfoRepositoryImpl implements MarketInfoRepository {

    private final ShapeShiftResource resource;


    /**
     * Default constructor, the params are provided by dagger
     * @param resource Retrofit resource  associated to the ShapeShift api
     */
    @Inject
    MarketInfoRepositoryImpl(ShapeShiftResource resource) {
        this.resource = resource;
    }



    /**
     * Get the list of available pairs from the server, format : BTC-ETH, in uppercase
     * @return  [Single] [List] [String] List of pairs available
     */
    @Override
    public Single<List<String>> validPairs() {
        return resource.validPairs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    /**
     * Get the market info given a pair of tokens
     * @param pair pair to get the market info
     * @return [Single] [MarketInfo] market info associated to this pair
     */
    @Override
    public Single<MarketInfo> marketInfo(String pair) {
        return resource.marketInfo(pair.toLowerCase())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
