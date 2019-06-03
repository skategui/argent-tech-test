package agis.guillaume.argent.usecase;

import agis.guillaume.argent.DataBuilder;
import agis.guillaume.argent.models.ERC20TokenTx;
import agis.guillaume.argent.models.requests.ERC20TokenResponse;
import agis.guillaume.argent.repo.AccountRepository;
import agis.guillaume.argent.rule.BaseRule;
import agis.guillaume.argent.storage.profile.AccountDataStore;
import agis.guillaume.argent.utils.WeiUtils;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

public class AccountUseCaseImplTest extends BaseRule {

    private AccountRepository repository = mock(AccountRepository.class);
    private AccountDataStore accountDataStore = mock(AccountDataStore.class);
    private TokenUseCase tokenUseCase = mock(TokenUseCase.class);

    private AccountUseCaseImpl accountUseCase;

    @Before
    public void setup() {
        accountUseCase = new AccountUseCaseImpl(tokenUseCase, repository, accountDataStore);

        when(tokenUseCase.validPairs()).thenReturn(Single.just(DataBuilder.generatePairs()));

    }


    @Test
    public void verifyEthBalanceIsCorrect() {
        BigDecimal ethBalance = new BigDecimal(42);
        BigDecimal expected = WeiUtils.convertWeiToETH(ethBalance);
        when(repository.getETHBalance()).thenReturn(Single.just(ethBalance));

        accountUseCase.getETHBalance()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(expected);


        verify(accountDataStore, times(1)).setBalanceEth(expected);
    }

    @Test
    public void verifyEthBalanceIsCorrectWithoutMakingTheRequestsTwice() {
        BigDecimal ethBalance = new BigDecimal(42);
        BigDecimal expected = WeiUtils.convertWeiToETH(ethBalance);
        when(repository.getETHBalance()).thenReturn(Single.just(ethBalance));

        accountUseCase.getETHBalance()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(expected);

        accountUseCase.getETHBalance()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(expected);


        verify(accountDataStore, times(1)).setBalanceEth(expected);
        verify(repository, times(1)).getETHBalance();
    }

    @Test
    public void verifyCorrectERC20TokensBalanceIsProvided() {

        BigDecimal expected = new BigDecimal(42);
        when(tokenUseCase.isValidPairEth(any())).thenReturn(true);

        when(tokenUseCase.getBalance(any(), any())).thenReturn(Single.just(expected));
        List<ERC20TokenTx> tokens = DataBuilder.generateERC20Tokens();

        ERC20TokenTx token = tokens.get(0);
        when(tokenUseCase.isValidPairEth(eq(token.getTokenSymbol()))).thenReturn(true);

        expected = expected.multiply(new BigDecimal(tokens.size()));

        ERC20TokenResponse erc20TokenResponse = new ERC20TokenResponse();
        erc20TokenResponse.setResult(tokens);


        when(repository.getERC20Tokens()).thenReturn(Single.just(erc20TokenResponse));

        accountUseCase.getTotalERC20TokenBalance()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(expected);

    }

    @Test
    public void verifyCorrectEthBalanceIsProvidedWithoutMakingTheRequestsTwice() {

        BigDecimal expected = new BigDecimal(42);
        when(tokenUseCase.isValidPairEth(any())).thenReturn(true);

        when(tokenUseCase.getBalance(any(), any())).thenReturn(Single.just(expected));
        List<ERC20TokenTx> tokens = DataBuilder.generateERC20Tokens();

        ERC20TokenTx token = tokens.get(0);
        when(tokenUseCase.isValidPairEth(eq(token.getTokenSymbol()))).thenReturn(true);

        expected = expected.multiply(new BigDecimal(tokens.size()));

        ERC20TokenResponse erc20TokenResponse = new ERC20TokenResponse();
        erc20TokenResponse.setResult(tokens);


        when(repository.getERC20Tokens()).thenReturn(Single.just(erc20TokenResponse));

        accountUseCase.getTotalERC20TokenBalance()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(expected);

        accountUseCase.getTotalERC20TokenBalance()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(expected);

        verify(repository, times(1)).getERC20Tokens();
    }
}