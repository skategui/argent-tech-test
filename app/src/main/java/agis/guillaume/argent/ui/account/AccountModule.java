package agis.guillaume.argent.ui.account;

import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.usecase.AccountUseCase;
import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class AccountModule {

    @Provides
    ViewModelProvider.Factory providesListPostsViewModelFactory(AccountUseCase accountUseCase,
                                                                HttpErrorUtils httpErrorUtils) {
        return new AccountViewModel.Factory(accountUseCase, httpErrorUtils);
    }

}
