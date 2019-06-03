package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.common.BaseViewModel;
import agis.guillaume.argent.models.ERC20TokenUser;
import agis.guillaume.argent.usecase.AccountUseCase;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.List;

/**
 * ViewModel responsible to load the list of valid pair against ETH
 * given the list of ERC 20 tokens for this account
 */
public class ListTokensViewModel extends BaseViewModel<ListTokensViewState> {

    private final HttpErrorUtils httpErrorUtils;
    private final AccountUseCase accountUseCase;

    /**
     * Private constructor, used only by its Factory
     *
     * @param httpErrorUtils responsible to check the HTTP errors
     */
    private ListTokensViewModel(AccountUseCase accountUseCase,
                                HttpErrorUtils httpErrorUtils) {
        this.httpErrorUtils = httpErrorUtils;
        this.accountUseCase = accountUseCase;
    }


    /**
     * Associated to the lifecycle of the activity; load the list of tokens when the activity
     * is created
     *
     * @param owner LifecycleOwner
     */
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        loadERC20TokenUser();
    }


    /**
     * Take the list of transactions from this account and create a mapping balance=token available
     * and generate a list that will be displayed in the UI
     */

    private void loadERC20TokenUser() {
        disposables.add(
                accountUseCase.getERC20Tokens()
                        .doOnSubscribe(t -> emitViewState(new ListTokensViewState.ShowLoading()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::success, this::errors)
        );
    }


    /**
     * Emit the list of tokens fetched
     *
     * @param list list to emit
     */
    private void success(List<ERC20TokenUser> list) {
        if (list.isEmpty())
            emitViewState(new ListTokensViewState.DisplayEmptyListMessage());
        else
            emitViewState(new ListTokensViewState.DisplayTokensList(list));
    }


    /**
     * Check if the type of error to throw the correct error asociated to the given Throwable
     *
     * @param throwable throwable thrown
     */
    private void errors(@NonNull Throwable throwable) {
        if (httpErrorUtils.hasLostInternet(throwable))
            emitViewState(new ListTokensViewState.NoInternet());
        else
            emitViewState(new ListTokensViewState.Error(throwable.getMessage()));
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
            return (T) new ListTokensViewModel(accountUseCase, httpErrorUtils);
        }
    }
}