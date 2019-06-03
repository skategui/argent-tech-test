package agis.guillaume.argent.api;

import agis.guillaume.argent.models.requests.MarketInfo;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * List of Api's from ShapeShift.io used in the app
 */
public interface ShapeShiftResource {

    @GET("validpairs")
    Single<List<String>> validPairs();

    @GET("marketinfo/{pair}")
    Single<MarketInfo> marketInfo(@Path("pair") String pair);
}
