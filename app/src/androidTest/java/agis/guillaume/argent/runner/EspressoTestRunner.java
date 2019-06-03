package agis.guillaume.argent.runner;

import agis.guillaume.argent.MainApplication;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import androidx.test.runner.AndroidJUnitRunner;

public class EspressoTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return Instrumentation.newApplication(MainApplication.class, context);
    }

}
