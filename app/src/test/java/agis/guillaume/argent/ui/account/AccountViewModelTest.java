package agis.guillaume.argent.ui.account;

import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.rule.BaseRule;
import agis.guillaume.argent.ui.account.AccountViewState.DisplayAccountBalance;
import agis.guillaume.argent.ui.account.AccountViewState.ViewMore;
import agis.guillaume.argent.usecase.AccountUseCase;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.UnknownHostException;

import static agis.guillaume.argent.ui.account.AccountViewState.ApiKeyNotProvided;
import static agis.guillaume.argent.ui.account.AccountViewState.DisplayERC20Balance;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

public class AccountViewModelTest extends BaseRule {


    private AccountUseCase accountUseCase = mock(AccountUseCase.class);

    private AccountViewModel viewModel;

    private LifecycleOwner lifecycleOwner = mock(LifecycleOwner.class);

    private BigDecimal ethBalance = new BigDecimal(42);
    private BigDecimal erc20TokenBalance = new BigDecimal(12);


    @Before
    public void setUp() {
        viewModel = spy(new AccountViewModel.Factory(accountUseCase,
                new HttpErrorUtils())
                .create(AccountViewModel.class));

        when(accountUseCase.getETHBalance()).thenReturn(Single.just(ethBalance));
        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.just(erc20TokenBalance));
        when(accountUseCase.getETHBalanceLocally()).thenReturn(ethBalance);
        when(accountUseCase.getTotalERC20TokenBalanceLocally()).thenReturn(erc20TokenBalance);
        when(viewModel.isApiProvided()).thenReturn(true);
        when(viewModel.isAccountAddrProvided()).thenReturn(true);


    }

    @Test
    public void GivenNoApiKeyWhenLoadingTheDataThenEmitAnError() {

        when(viewModel.isApiProvided()).thenReturn(false);

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);

        AccountViewState state = testObserver
                .assertNoErrors()
                .values()
                .get(2);

        assertThat(state instanceof ApiKeyNotProvided).isTrue();
    }

    @Test
    public void GivenNoAccountAddrWhenLoadingTheDataThenEmitAnError() {

        when(viewModel.isAccountAddrProvided()).thenReturn(false);

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);

        AccountViewState state = testObserver
                .assertNoErrors()
                .values()
                .get(2);

        assertThat(state instanceof AccountViewState.AccountAddrProvided).isTrue();
    }

    @Test
    public void GivenSomeDataStoredLocallyWhenLoadingTheDataThenEmitThoseData() {

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();


        viewModel.onCreate(lifecycleOwner);

        AccountViewState state1 = testObserver
                .assertNoErrors()
                .values()
                .get(0);

        assertThat(state1 instanceof DisplayAccountBalance).isTrue();
        assertThat(((DisplayAccountBalance) state1).getValue()).isEqualTo(ethBalance);

        AccountViewState state2 = testObserver
                .assertNoErrors()
                .values()
                .get(1);

        assertThat(state2 instanceof DisplayERC20Balance).isTrue();
        assertThat(((DisplayERC20Balance) state2).getValue()).isEqualTo(erc20TokenBalance);
    }


    @Test
    public void GivenSomeDataWhenFetchingTheDataFromServerThenEmitThoseData() {

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);

        AccountViewState stateLoading = testObserver
                .assertNoErrors()
                .values()
                .get(2);

        assertThat(stateLoading instanceof AccountViewState.ShowLoading).isTrue();

        AccountViewState ethBalanceState = testObserver
                .assertNoErrors()
                .values()
                .get(3);

        assertThat(ethBalanceState instanceof DisplayAccountBalance).isTrue();
        assertThat(((DisplayAccountBalance) ethBalanceState).getValue()).isEqualTo(ethBalance);

        AccountViewState stateLoading2 = testObserver
                .assertNoErrors()
                .values()
                .get(4);

        assertThat(stateLoading2 instanceof AccountViewState.ShowLoading).isTrue();


        AccountViewState erc20BalanceState = testObserver
                .assertNoErrors()
                .values()
                .get(5);

        assertThat(erc20BalanceState instanceof DisplayERC20Balance).isTrue();
        assertThat(((DisplayERC20Balance) erc20BalanceState).getValue()).isEqualTo(erc20TokenBalance);
    }


    @Test
    public void GivenSomethingWhenClickingRefreshButtonThenReloadTheData() {

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onRefreshClicked();

        AccountViewState stateLoading = testObserver
                .assertNoErrors()
                .values()
                .get(0);

        assertThat(stateLoading instanceof AccountViewState.ShowLoading).isTrue();

        AccountViewState ethBalanceState = testObserver
                .assertNoErrors()
                .values()
                .get(1);

        assertThat(ethBalanceState instanceof DisplayAccountBalance).isTrue();
        assertThat(((DisplayAccountBalance) ethBalanceState).getValue()).isEqualTo(ethBalance);

        AccountViewState stateLoading2 = testObserver
                .assertNoErrors()
                .values()
                .get(2);

        assertThat(stateLoading2 instanceof AccountViewState.ShowLoading).isTrue();


        AccountViewState erc20BalanceState = testObserver
                .assertNoErrors()
                .values()
                .get(3);

        assertThat(erc20BalanceState instanceof DisplayERC20Balance).isTrue();
        assertThat(((DisplayERC20Balance) erc20BalanceState).getValue()).isEqualTo(erc20TokenBalance);
    }


    @Test
    public void GivenEnErrorGettingTheEthBalanceFromServerThenEmitTheErrorWithTheMessage() {

        String errorMSg = "msg";
        Exception exception = new Exception(errorMSg);

        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.error(exception));
        when(accountUseCase.getETHBalance()).thenReturn(Single.error(exception));

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);

        AccountViewState state = testObserver
                .assertNoErrors()
                .values()
                .get(3);

        assertThat(state instanceof AccountViewState.Error).isTrue();
        assertThat(((AccountViewState.Error) state).getError()).isEqualTo(errorMSg);
    }

    @Test
    public void GivenEnErrorGettingTheERC20BalanceFromServerThenEmitTheErrorWithTheMessage() {

        String errorMSg = "msg";
        Exception exception = new Exception(errorMSg);

        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.error(exception));
        when(accountUseCase.getETHBalance()).thenReturn(Single.error(exception));

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);


        AccountViewState state = testObserver
                .assertNoErrors()
                .values()
                .get(3);

        assertThat(state instanceof AccountViewState.Error).isTrue();
        assertThat(((AccountViewState.Error) state).getError()).isEqualTo(errorMSg);
    }


    @Test
    public void GivenALostOfconnexionGettingTheEthBalanceFromServerThenEmitTheErrorWithTheMessage() {

        String errorMSg = "msg";
        Exception exception = new UnknownHostException(errorMSg);

        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.error(exception));
        when(accountUseCase.getETHBalance()).thenReturn(Single.error(exception));

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();


        viewModel.onCreate(lifecycleOwner);

        AccountViewState state = testObserver
                .assertNoErrors()
                .values()
                .get(3);

        assertThat(state instanceof AccountViewState.NoInternet).isTrue();
    }

    @Test
    public void GivenALostOfconnexionGettingTheERC20BalanceFromServerThenEmitTheErrorWithTheMessage() {

        String errorMSg = "msg";
        Exception exception = new UnknownHostException(errorMSg);

        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.error(exception));
        when(accountUseCase.getETHBalance()).thenReturn(Single.error(exception));

        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();


        viewModel.onCreate(lifecycleOwner);

        AccountViewState state = testObserver
                .assertNoErrors()
                .values()
                .get(3);

        assertThat(state instanceof AccountViewState.NoInternet).isTrue();
    }

    @Test
    public void WhenClickingTheViewMoreButtonThenEmitViewMoreState() {
        TestObserver<AccountViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onViewMoreClicked();
        AccountViewState state = testObserver
                .assertNoErrors()
                .values()
                .get(0);

        assertThat(state instanceof ViewMore).isTrue();
    }


}