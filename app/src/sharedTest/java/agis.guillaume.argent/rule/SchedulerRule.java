package agis.guillaume.argent.rule;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SchedulerRule implements TestRule {

    /**
     * All jobs that subscribes on trampoline() will be queued and executed one by one.
     */
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(v -> Schedulers.trampoline());
                RxJavaPlugins.setComputationSchedulerHandler(v -> Schedulers.trampoline());
                RxJavaPlugins.setNewThreadSchedulerHandler(v -> Schedulers.trampoline());
                RxAndroidPlugins.setMainThreadSchedulerHandler(v -> Schedulers.trampoline());
                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}