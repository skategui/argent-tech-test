package agis.guillaume.argent.rule;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import org.junit.Rule;

public class BaseRule {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Rule
    public  SchedulerRule schedulerRule = new SchedulerRule();
}
