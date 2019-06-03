package agis.guillaume.argent.di;

import agis.guillaume.argent.repo.*;
import agis.guillaume.argent.repo.MarketInfoRepository;
import agis.guillaume.argent.repo.MarketInfoRepositoryImpl;
import dagger.Binds;
import dagger.Module;

@Module
abstract class RepositoryModule {


    @Binds
    abstract AccountRepository bindAccountRepository(AccountRepositoryImpl impl);

    @Binds
    abstract MarketInfoRepository bindTokenRepository(MarketInfoRepositoryImpl impl);
}
