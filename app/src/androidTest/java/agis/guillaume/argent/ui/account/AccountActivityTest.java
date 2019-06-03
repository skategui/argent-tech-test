package agis.guillaume.argent.ui.account;

import agis.guillaume.argent.MainApplication;
import agis.guillaume.argent.R;
import agis.guillaume.argent.api.HttpErrorUtils;
import agis.guillaume.argent.usecase.AccountUseCase;
import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import io.reactivex.Single;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.net.UnknownHostException;

import static agis.guillaume.argent.ui.utils.ExtraAssertions.isGone;
import static agis.guillaume.argent.ui.utils.ExtraAssertions.isVisible;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class AccountActivityTest {

    private AccountUseCase accountUseCase = mock(AccountUseCase.class);


    private BigDecimal ethBalance = new BigDecimal(42.12);
    private BigDecimal erc20Tokens = new BigDecimal(101.13);


    private AccountViewModel.Factory testViewModelFactory = new AccountViewModel.Factory(accountUseCase, new HttpErrorUtils());

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(AccountActivity.class, false, false);

    private Activity initDispatcherAndLaunchActivity() {
        MainApplication myApp =
                (MainApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        myApp.dispatchingAndroidInjector = AccountActivityUtils.initDispatcher(testViewModelFactory);
        return activityTestRule.launchActivity(null);
    }


    @Test
    public void given_a_connexion_when_fetching_account_data_then_display_the_right_data() {

        when(accountUseCase.getETHBalanceLocally()).thenReturn(ethBalance);
        when(accountUseCase.getTotalERC20TokenBalanceLocally()).thenReturn(erc20Tokens);
        when(accountUseCase.getETHBalance()).thenReturn(Single.just(ethBalance));
        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.just(erc20Tokens));

        Activity activity = initDispatcherAndLaunchActivity();

        onView(withId(R.id.llState)).check(matches(not(isVisible())));
        onView(withId(R.id.llErcToken)).check(matches(isDisplayed()));

        onView(withId(R.id.tvAccountEth)).check(matches(withText(
                String.format(activity.getResources().getString(R.string.account_amount_eth), ethBalance.doubleValue())))
        );
        onView(withId(R.id.tvErc20Balance)).check(matches(withText(
                String.format(activity.getResources().getString(R.string.account_amount_eth), erc20Tokens.doubleValue())))
        );
        onView(withId(R.id.tvViewMore)).check(matches(isClickable()));
    }

    @Test
    public void given_a_lost_of_connexion_when_fetching_account_data_then_display_a_msg() {

        BigDecimal ethBalanceLocally = new BigDecimal(42.12);
        BigDecimal erc20TokensLocally = new BigDecimal(101.13);

        when(accountUseCase.getETHBalanceLocally()).thenReturn(ethBalanceLocally);
        when(accountUseCase.getTotalERC20TokenBalanceLocally()).thenReturn(erc20TokensLocally);
        when(accountUseCase.getETHBalance()).thenReturn(Single.error(new UnknownHostException()));
        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.error(new UnknownHostException()));

        initDispatcherAndLaunchActivity();

        onView(withId(R.id.llState)).check(isGone());

        onView(withId(R.id.llPopupState)).check(matches(isDisplayed()));
        onView(withId(R.id.tvPopupStateTitle)).check(matches(withText(R.string.error_no_internet_connexion)));
    }

    @Test
    public void given_an_error_when_fetching_account_data_then_display_a_generic_error_msg() {

        BigDecimal ethBalanceLocally = new BigDecimal(42.12);
        BigDecimal erc20TokensLocally = new BigDecimal(101.13);

        when(accountUseCase.getETHBalanceLocally()).thenReturn(ethBalanceLocally);
        when(accountUseCase.getTotalERC20TokenBalanceLocally()).thenReturn(erc20TokensLocally);
        when(accountUseCase.getETHBalance()).thenReturn(Single.error(new RuntimeException()));
        when(accountUseCase.getTotalERC20TokenBalance()).thenReturn(Single.error(new RuntimeException()));

        initDispatcherAndLaunchActivity();

        onView(withId(R.id.llState)).check(isGone());

        onView(withId(R.id.llPopupState)).check(matches(isDisplayed()));
        onView(withId(R.id.tvPopupStateTitle)).check(matches(withText(R.string.error_try_again_later)));
    }

    // add espresso test to check the UI when the Account Addr is not provided
    // Add espresso test to check the UI when the API Key  is not provided

}