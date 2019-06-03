package agis.guillaume.argent.di;

import agis.guillaume.argent.R;
import agis.guillaume.argent.storage.profile.AccountDataStore;
import agis.guillaume.argent.storage.profile.AccountDataStoreImpl;
import agis.guillaume.argent.storage.token.MarketDataStore;
import agis.guillaume.argent.storage.token.MarketDataStoreImpl;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;


@Module
class DatastoreModule {


    @Provides
    AccountDataStore createProfileDataStore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return new AccountDataStoreImpl(sharedPreferences, new Gson());
    }

    @Provides
    MarketDataStore createTokenDataStore() {
        return new MarketDataStoreImpl();
    }

}