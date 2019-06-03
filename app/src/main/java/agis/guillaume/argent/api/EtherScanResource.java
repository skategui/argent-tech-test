package agis.guillaume.argent.api;

import agis.guillaume.argent.models.requests.BalanceResponse;
import agis.guillaume.argent.models.requests.ERC20TokenResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * List of API's from etherscan.io used in the app
 */
public interface EtherScanResource {

    String accountModule = "account";

    @GET("api?module=" + accountModule + "&action=balance")
    Single<BalanceResponse> getETHBalance(@Query("address") String address, @Query("apikey") String apikey);

    @GET("api?module=" + accountModule + "&action=tokentx&")
    Single<ERC20TokenResponse> getERC20(@Query("address") String address, @Query("apikey") String apikey);

}
