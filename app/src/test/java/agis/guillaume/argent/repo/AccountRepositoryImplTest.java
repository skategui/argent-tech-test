package agis.guillaume.argent.repo;

import agis.guillaume.argent.BuildConfig;
import agis.guillaume.argent.api.EtherScanResource;
import agis.guillaume.argent.models.ERC20TokenTx;
import agis.guillaume.argent.models.requests.BalanceResponse;
import agis.guillaume.argent.models.requests.ERC20TokenResponse;
import agis.guillaume.argent.rule.BaseRule;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountRepositoryImplTest extends BaseRule {

    private EtherScanResource resource = mock(EtherScanResource.class);

    private AccountRepository accountRepository;

    @Before
    public void setup() {
        accountRepository = new AccountRepositoryImpl(resource);
    }


    @Test
    public void verifyEthBalanceIsCorrect()
    {
        BigDecimal value = new BigDecimal(42);
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setResult("" + value);

        when(resource.getETHBalance(eq(BuildConfig.ACCOUNT_ADDR), eq(BuildConfig.ETHERSCAN_API_KEY))).thenReturn(Single.just(balanceResponse));

        accountRepository.getETHBalance()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(value);
    }

    @Test
    public void verifyConnectEthBalanceIsProvided()
    {
        ERC20TokenTx btc = new ERC20TokenTx();
        btc.setTokenSymbol("BTC");

        ERC20TokenTx eth = new ERC20TokenTx();
        eth.setTokenSymbol("ETH");

        List<ERC20TokenTx> tokens = new ArrayList<>();
        tokens.add(btc);
        tokens.add(eth);

        ERC20TokenResponse erc20TokenResponse = new ERC20TokenResponse();
        erc20TokenResponse.setResult(tokens);

        when(resource.getERC20(eq(BuildConfig.ACCOUNT_ADDR), eq(BuildConfig.ETHERSCAN_API_KEY))).thenReturn(Single.just(erc20TokenResponse));

        accountRepository.getERC20Tokens()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(erc20TokenResponse);
    }

}