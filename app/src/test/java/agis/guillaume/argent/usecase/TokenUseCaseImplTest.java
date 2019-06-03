package agis.guillaume.argent.usecase;

import agis.guillaume.argent.DataBuilder;
import agis.guillaume.argent.repo.MarketInfoRepository;
import agis.guillaume.argent.rule.BaseRule;
import agis.guillaume.argent.storage.token.MarketDataStoreImpl;
import com.google.common.truth.Truth;
import io.reactivex.Single;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class TokenUseCaseImplTest extends BaseRule {

    private MarketInfoRepository marketInfoRepository = mock(MarketInfoRepository.class);

    private TokenUseCase tokenUseCase;

    private List<String> pairs = DataBuilder.generatePairs();

    @Before
    public void setup() {
        MarketDataStoreImpl marketDataStore = new MarketDataStoreImpl();
        tokenUseCase = new TokenUseCaseImpl(marketInfoRepository, marketDataStore);
    }

    @After
    public void tearsDown() {
        tokenUseCase = null;
    }


    @Test
    public void verifyGetCorrectPairsFromServer() {

        when(marketInfoRepository.validPairs()).thenReturn(Single.just(pairs));

        tokenUseCase.validPairs()
                .test()
                .assertNoErrors()
                .assertValue(pairs);

        verify(marketInfoRepository, times(1)).validPairs();
    }

    @Test
    public void verifyGetCorrectPairsFromDataStore() {

        when(marketInfoRepository.validPairs()).thenReturn(Single.just(pairs));

        tokenUseCase.validPairs()
                .test()
                .assertNoErrors()
                .assertValue(pairs);

        tokenUseCase.validPairs()
                .test()
                .assertNoErrors()
                .assertValue(pairs);


        verify(marketInfoRepository, times(1)).validPairs();
    }


    @Test
    public void verifyCanValidCorrectPair() {

        when(marketInfoRepository.validPairs()).thenReturn(Single.just(pairs));

        tokenUseCase.validPairs().concatWith(__ -> Truth.assertThat(tokenUseCase.isValidPairEth("btc")).isTrue());
    }

    @Test
    public void verifyCantValidInvalidPair() {

        when(marketInfoRepository.validPairs()).thenReturn(Single.just(pairs));

        tokenUseCase.validPairs().concatWith(__ -> Truth.assertThat(tokenUseCase.isValidPairEth("nope")).isFalse());
    }

}