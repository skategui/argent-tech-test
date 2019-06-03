package agis.guillaume.argent.di;

import agis.guillaume.argent.MainApplication;
import agis.guillaume.argent.ui.account.AccountModule;
import agis.guillaume.argent.ui.list.TokensListModule;
import android.content.Context;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

/**
 * dependencies needed in the app
 */
@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                TokensListModule.class,
                AccountModule.class,
                NetworkModule.class,
                RepositoryModule.class,
                ActivityModule.class,
                UseCaseModule.class,
                DatastoreModule.class
        }
)
public interface MainAppComponent extends AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        MainAppComponent build();
    }
}
