package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.DataBuilder;
import agis.guillaume.argent.MainApplication;
import agis.guillaume.argent.R;
import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.models.ERC20TokenUser;
import agis.guillaume.argent.ui.utils.RecyclerViewMatcher;
import agis.guillaume.argent.usecase.AccountUseCase;
import agis.guillaume.argent.utils.DiffCallback;
import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import io.reactivex.Single;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ListTokensActivityTest {
    private AccountUseCase accountUseCase = mock(AccountUseCase.class);

    private ListTokensViewModel.Factory testViewModelFactory = new ListTokensViewModel.Factory(accountUseCase, new HttpErrorUtils());

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(ListTokensActivity.class, false, false);

    private Activity initDispatcherAndLaunchActivity() {
        MainApplication myApp =
                (MainApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        myApp.dispatchingAndroidInjector = ListTokensActivityUtils.initDispatcher(
                testViewModelFactory,
                new ListTokensAdapterImpl(myApp.getApplicationContext(), new DiffCallback())
        );
        return activityTestRule.launchActivity(null);
    }


    @Test
    public void given_some_erc_20_tokens_then_display_the_valid_pairs_in_a_list() {

        List<ERC20TokenUser> list = DataBuilder.getErc20TokensWithBalance();

        when(accountUseCase.getERC20Tokens()).thenReturn(Single.just(list));

        Activity activity = initDispatcherAndLaunchActivity();

        onView(withId(R.id.llState)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recyclerTokenView)).check(matches(isDisplayed()));

        RecyclerViewMatcher matcher = new RecyclerViewMatcher(R.id.recyclerTokenView);

        for (int i = 0; i < list.size(); i++) {
            ERC20TokenUser token = list.get(i);
            onView(matcher
                    .atPositionOnView(i, R.id.tvTokenName))
                    .check(matches(withText(String.format(
                            activity.getString(R.string.item_token_title), token.getName(), token.getSymbol())))
                    );


            onView(matcher
                    .atPositionOnView(i, R.id.tvBalance))
                    .check(matches(withText(String.format(
                            activity.getResources().getString(R.string.account_amount_eth), token.getBalance())))
                    );

            onView(matcher
                    .atPositionOnView(i, R.id.ivInfo))
                    .check(matches(not(isDisplayed())));

            onView(matcher
                    .atPositionOnView(i, R.id.tvAmount))
                    .check(matches(withText(
                            String.format(activity.getString(R.string.item_token_nb), token.getAmount())))
                    );
        }
    }


    @Test
    public void display_token_with_warning_icon() {

        ERC20TokenUser token = new ERC20TokenUser("OMG", "OmiseGo", new BigDecimal(23123), new BigDecimal(-1));

        ArrayList<ERC20TokenUser> list = new ArrayList<>();
        list.add(token);

        when(accountUseCase.getERC20Tokens()).thenReturn(Single.just(list));

        Activity activity = initDispatcherAndLaunchActivity();

        onView(withId(R.id.llState)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recyclerTokenView)).check(matches(isDisplayed()));

        RecyclerViewMatcher matcher = new RecyclerViewMatcher(R.id.recyclerTokenView);

        onView(matcher
                .atPositionOnView(0, R.id.tvTokenName))
                .check(matches(withText(String.format(
                        activity.getString(R.string.item_token_title), token.getName(), token.getSymbol())))
                );

        onView(matcher
                .atPositionOnView(0, R.id.tvBalance))
                .check(matches(not(isDisplayed())));

        onView(matcher
                .atPositionOnView(0, R.id.tvAmount))
                .check(matches(isDisplayed()));

        onView(matcher
                .atPositionOnView(0, R.id.ivInfo))
                .check(matches(isDisplayed()));
    }

    @Test
    public void given_no_tokens_then_display_a_message_for_empty_list() {
        when(accountUseCase.getERC20Tokens()).thenReturn(Single.just(new ArrayList<>()));
        initDispatcherAndLaunchActivity();

        onView(withId(R.id.llState)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerTokenView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tvStateTitle)).check(matches(withText((R.string.error_no_token_available))));

    }

    // add test to verify there is a message if there is no internet connexion when loading the ERC 20 tokens with their balance

}