package agis.guillaume.argent.ui.account;

import agis.guillaume.argent.BuildConfig;
import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.common.BaseViewModel;
import agis.guillaume.argent.usecase.AccountUseCase;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

/**
 * Responsible to load the data for the Account view
 */
public class AccountViewModel extends BaseViewModel<AccountViewState> {


    private final HttpErrorUtils httpErrorUtils;
    private final AccountUseCase accountUseCase;


    private AccountViewModel(AccountUseCase accountUseCase,
                             HttpErrorUtils httpErrorUtils) {
        this.httpErrorUtils = httpErrorUtils;
        this.accountUseCase = accountUseCase;
    }


    /**
     * Associated to the lifecycle of the activity; load the ETH balance of the account when the activity
     * is created
     * Verify that the required informations are provided in the settings to make this app working correctly
     * before being able to load any data
     *
     * @param owner LifecycleOwner
     */
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

        boolean apiKeyProvided = isApiProvided();

        boolean accountAddrProvided = isAccountAddrProvided();

        emitViewState(new AccountViewState.DisplayAccountBalance(accountUseCase.getETHBalanceLocally()));
        emitViewState(new AccountViewState.DisplayERC20Balance(accountUseCase.getTotalERC20TokenBalanceLocally()));


        if (apiKeyProvided && accountAddrProvided) {
            loadData();
        }
        if (!apiKeyProvided)
            emitViewState(new AccountViewState.ApiKeyNotProvided());
        else if (!accountAddrProvided)
            emitViewState(new AccountViewState.AccountAddrProvided());
    }

    /**
     * Check if the API key has been provided into the variables
     *
     * @return true if the API key has been provided, false otherwise
     */
    @VisibleForTesting
    boolean isApiProvided() {
        return !BuildConfig.ETHERSCAN_API_KEY.isEmpty();
    }


    /**
     * Check if the Account ADDR has been provided into the variables
     *
     * @return true if the Account ADDR has been provided, false otehrwise
     */
    @VisibleForTesting
    boolean isAccountAddrProvided() {
        return !BuildConfig.ACCOUNT_ADDR.isEmpty();
    }

    /**
     * Load all the data that needs to be displayed on the view
     */
    private void loadData() {
        getEthBalance();
        getTotalERC20TokenBalance();
    }


    /**
     * Get the ETH balance associated to the user and emit the different state of this process
     */
    private void getEthBalance() {
        disposables.add(
                accountUseCase.getETHBalance()
                        .doOnSubscribe(t -> emitViewState(new AccountViewState.ShowLoading()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(balance -> emitViewState(new AccountViewState.DisplayAccountBalance(balance)),
                                this::receivedError)
        );
    }


    /**
     * Get the aggreagtions of the balances of ERC 20 tokens associated to the user
     * and emit the new state with the balance
     */
    private void getTotalERC20TokenBalance() {
        disposables.add(
                accountUseCase.getTotalERC20TokenBalance()
                        .doOnSubscribe(t -> emitViewState(new AccountViewState.ShowLoading()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(balance -> emitViewState(new AccountViewState.DisplayERC20Balance(balance)),
                                this::receivedError)
        );
    }


    /**
     * Emit the error with the message to the view
     *
     * @param throwable error thrown
     */
    private void receivedError(Throwable throwable) {
        if (httpErrorUtils.hasLostInternet(throwable))
            emitViewState(new AccountViewState.NoInternet());
        else
            emitViewState(new AccountViewState.Error(throwable.getMessage()));
    }

    /**
     * Callback when the user click on the "view more" button. emit a new state associated to it
     */
    void onViewMoreClicked() {
        emitViewState(new AccountViewState.ViewMore());
    }

    /**
     * Callback when the user clicks the "refresh" button. Emit a new state associated to it
     */
    void onRefreshClicked() {
        loadData();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final HttpErrorUtils httpErrorUtils;
        private final AccountUseCase accountUseCase;

        @Inject
        public Factory(AccountUseCase accountUseCase,
                       HttpErrorUtils httpErrorUtils) {
            this.httpErrorUtils = httpErrorUtils;
            this.accountUseCase = accountUseCase;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AccountViewModel(accountUseCase, httpErrorUtils);
        }
    }
}