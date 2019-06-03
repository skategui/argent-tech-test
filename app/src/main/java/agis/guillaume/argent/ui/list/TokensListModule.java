package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.di.DiffCallbackModule;
import agis.guillaume.argent.usecase.AccountUseCase;
import agis.guillaume.argent.utils.DiffCallback;
import android.content.Context;
import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;

@Module(includes = {DiffCallbackModule.class})
public class TokensListModule {

    @Provides
    ViewModelProvider.Factory providesListTokensViewModelFactory(HttpErrorUtils httpErrorUtils,
                                                                 AccountUseCase accountUseCase) {
        return new ListTokensViewModel.Factory(accountUseCase, httpErrorUtils);
    }


    @Provides
    ListTokensAdapter provideListTokensAdapter(Context context, DiffCallback diffCallback) {
        return new ListTokensAdapterImpl(context, diffCallback);
    }
}
