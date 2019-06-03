package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.DataBuilder;
import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.models.Coin;
import agis.guillaume.argent.rule.BaseRule;
import agis.guillaume.argent.ui.list.ListTokensViewState.DisplayTokensList;
import agis.guillaume.argent.usecase.AccountUseCase;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListTokensViewModelTest extends BaseRule {

    private AccountUseCase accountUseCase = mock(AccountUseCase.class);
    private LifecycleOwner lifecycleOwner = mock(LifecycleOwner.class);
    private ListTokensViewModel viewModel;

    private List<String> validPairs = DataBuilder.generatePairs();
    private List<Coin> balanceByToken = DataBuilder.getErc20TokensWithBalance();


    @Before
    public void setUp() {
        viewModel = new ListTokensViewModel.Factory(accountUseCase, new HttpErrorUtils())
                .create(ListTokensViewModel.class);

        when(accountUseCase.loadOwnedCoins()).thenReturn(Single.just(balanceByToken));

    }

    @After
    public void tearsDown() {
        validPairs.clear();

    }

    @Test
    public void WhenCreatingTheViewThenLoadAllTheCoins() {

        TestObserver<ListTokensViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);

        ListTokensViewState loadingState = testObserver
                .assertNoErrors()
                .values()
                .get(0);

        assertThat(loadingState instanceof ListTokensViewState.ShowLoading).isTrue();

        ListTokensViewState displayListState = testObserver
                .assertNoErrors()
                .values()
                .get(1);

        assertThat(displayListState instanceof DisplayTokensList).isTrue();
        List<Coin> list = ((DisplayTokensList) displayListState).getList();
        assertThat(list.size()).isEqualTo(balanceByToken.size());

        for (Coin coin : list) {
            assertThat(balanceByToken.contains(coin)).isTrue();
        }
    }

    @Test
    public void GivenEnErrorWhenMakingTheRequestsThenEmitTheError() {

        String errorMSg = "msg";
        Exception exception = new Exception(errorMSg);

        when(accountUseCase.loadOwnedCoins()).thenReturn(Single.error(exception));

        TestObserver<ListTokensViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);


        ListTokensViewState errorState = testObserver
                .assertNoErrors()
                .values()
                .get(1);

        assertThat(errorState instanceof ListTokensViewState.Error).isTrue();
        assertThat(((ListTokensViewState.Error) errorState).getError()).isEqualTo(errorMSg);

    }


    @Test
    public void GivenAConnexionErrorWhenMakingTheRequestsThenEmitTheError() {

        String errorMSg = "msg";
        Exception exception = new UnknownHostException(errorMSg);

        when(accountUseCase.loadOwnedCoins()).thenReturn(Single.error(exception));

        TestObserver<ListTokensViewState> testObserver = viewModel.getViewStateObservable().test();

        viewModel.onCreate(lifecycleOwner);


        ListTokensViewState errorState = testObserver
                .assertNoErrors()
                .values()
                .get(1);

        assertThat(errorState instanceof ListTokensViewState.NoInternet).isTrue();

    }

}