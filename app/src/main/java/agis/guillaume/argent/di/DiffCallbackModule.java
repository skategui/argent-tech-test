package agis.guillaume.argent.di;

import agis.guillaume.argent.utils.DiffCallback;
import dagger.Module;
import dagger.Provides;

@Module
public class DiffCallbackModule {

    @Provides
    DiffCallback providesDiffCallbackModule() {
        return new DiffCallback();
    }
}
