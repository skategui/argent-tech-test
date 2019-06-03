package agis.guillaume.argent;

import agis.guillaume.argent.di.DaggerMainAppComponent;
import agis.guillaume.argent.di.MainAppComponent;
import android.app.Activity;
import android.app.Application;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import javax.inject.Inject;

public class MainApplication extends Application implements HasActivityInjector {

    // to be able to inject dep in the activities
    @Inject
    public DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    static MainAppComponent mainAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
    }

    // setup the DI..
    private void initDagger() {
        mainAppComponent = DaggerMainAppComponent.builder()
                .context(this)
                .build();
        mainAppComponent.inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}