package agis.guillaume.argent.di;

import agis.guillaume.argent.usecase.AccountUseCase;
import agis.guillaume.argent.usecase.AccountUseCaseImpl;
import agis.guillaume.argent.usecase.TokenUseCase;
import agis.guillaume.argent.usecase.TokenUseCaseImpl;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
abstract class UseCaseModule {


    @Singleton
    @Binds
    abstract AccountUseCase bindAccountUseCase(AccountUseCaseImpl impl);

    @Binds
    abstract TokenUseCase bindTokenUseCase(TokenUseCaseImpl impl);
}
