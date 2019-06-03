package agis.guillaume.argent.di;

import agis.guillaume.argent.ui.account.AccountActivity;
import agis.guillaume.argent.ui.list.ListTokensActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract AccountActivity contributeAccountActivityInjector();

    @ContributesAndroidInjector
    abstract ListTokensActivity contributeListTokensActivityInjector();
}
