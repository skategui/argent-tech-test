package agis.guillaume.argent.repo;

import agis.guillaume.argent.DataBuilder;
import agis.guillaume.argent.api.ShapeShiftResource;
import agis.guillaume.argent.models.requests.MarketInfo;
import agis.guillaume.argent.rule.BaseRule;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarketInfoRepositoryImplTest extends BaseRule {

    private ShapeShiftResource resource = mock(ShapeShiftResource.class);

    private MarketInfoRepository marketInfoRepository;

    @Before
    public void setup() {
        marketInfoRepository = new MarketInfoRepositoryImpl(resource);
    }


    @Test
    public void verifyGetPairs() {
        List<String> pairs = DataBuilder.generatePairs();

        when(resource.validPairs()).thenReturn(Single.just(pairs));

        marketInfoRepository.validPairs()
                .test()
                .assertNoErrors()
                .assertValue(pairs);
    }


    @Test
    public void verifyMarketInfowithPairUpperCase() {

        String pair = "BTC-ETH";
        String pairLowerCase = pair.toLowerCase();

        MarketInfo info = new MarketInfo();
        info.setPair(pair);
        info.setRate(12.42);


        when(resource.marketInfo(eq(pairLowerCase))).thenReturn(Single.just(info));

        marketInfoRepository.marketInfo(pair)
                .test()
                .assertNoErrors()
                .assertValue(info);
    }

    @Test
    public void verifyMarketInfowithPairLowerCase() {

        String pair = "btc-eth";
        String pairLowerCase = pair.toLowerCase();

        MarketInfo info = new MarketInfo();
        info.setPair(pair);
        info.setRate(12.42);


        when(resource.marketInfo(eq(pairLowerCase))).thenReturn(Single.just(info));

        marketInfoRepository.marketInfo(pair)
                .test()
                .assertNoErrors()
                .assertValue(info);
    }

}