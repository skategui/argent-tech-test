package agis.guillaume.argent.ui.account;

import agis.guillaume.argent.R;
import agis.guillaume.argent.common.BaseActivity;
import agis.guillaume.argent.ui.custom.ErrorPopup;
import agis.guillaume.argent.ui.list.ListTokensActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import com.airbnb.lottie.LottieAnimationView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static com.airbnb.lottie.LottieDrawable.REVERSE;

/**
 * View associated to the account, the first page when the app is opened
 */
public class AccountActivity extends BaseActivity {

    @Inject
    AccountViewModel.Factory viewModelFactory;

    private AccountViewModel viewModel;

    @BindView(R.id.animation)
    LottieAnimationView animation;

    @BindView(R.id.llState)
    LinearLayout llState;


    @BindView(R.id.tvStateTitle)
    TextView tvStateTitle;

    @BindView(R.id.tvViewMore)
    TextView tvViewMore;

    @BindView(R.id.llErcToken)
    LinearLayout llErcToken;

    @BindView(R.id.tvAccountEth)
    TextView tvAccountEth;

    @BindView(R.id.tvErc20Balance)
    TextView tvErc20Balance;


    private ErrorPopup errorPopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
        initViewModel();
    }

    /**
     * Init the view
     */
    private void init() {
        initAnimation();
        initViews();
        initListeners();
    }

    private void initListeners() {
        tvViewMore.setOnClickListener(v -> viewModel.onViewMoreClicked());
    }


    /**
     * Init animation
     */
    private void initAnimation() {
        // set animation
        animation.setRepeatMode(REVERSE);
        animation.setRepeatCount(INFINITE);
    }


    /**
     * init viewmodel
     */
    private void initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel.class);
        getLifecycle().addObserver(viewModel);
        disposables.add(
                viewModel.getViewStateObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::render, Throwable::printStackTrace)
        );
    }

    /**
     * Render the view with the actions given the current state of the view
     * @param viewState current state of the view
     *
     * MVI architecture : ViewState associated to the list tokens
     * (Looks better in kotlin, using when/is keywords instead of this ugly list of if/else if ... )
     *
     */
    private void render(AccountViewState viewState) {
        clearAnimation();

        if (viewState instanceof AccountViewState.ShowLoading) {
            checkLoadingState(true);
        } else if (viewState instanceof AccountViewState.DisplayAccountBalance) {
            AccountViewState.DisplayAccountBalance state = (AccountViewState.DisplayAccountBalance) viewState;
            setEthBalance(state.getValue().doubleValue());
        } else if (viewState instanceof AccountViewState.DisplayERC20Balance) {
            AccountViewState.DisplayERC20Balance state = (AccountViewState.DisplayERC20Balance) viewState;
            setErc20Balance(state.getValue().doubleValue());
        } else if (viewState instanceof AccountViewState.ViewMore) {
            ListTokensActivity.start(this);
        } else if (viewState instanceof AccountViewState.Error) {
            displayError(R.string.error_try_again_later);
        } else if (viewState instanceof AccountViewState.NoInternet) {
            noInternet();
        }
        else if (viewState instanceof AccountViewState.ApiKeyNotProvided) {
            displayError(R.string.error_api_key_not_found);
        }
        else if (viewState instanceof AccountViewState.AccountAddrProvided) {
            displayError(R.string.error_account_addr_not_found);
        }
    }

    private void setEthBalance(double amount) {
        tvAccountEth.setText(String.format(getString(R.string.account_amount_eth), amount));
        tvAccountEth.setVisibility(View.VISIBLE);
    }

    private void setErc20Balance(double amount) {
        tvErc20Balance.setText(String.format(getString(R.string.account_amount_eth), amount));
        tvErc20Balance.setVisibility(View.VISIBLE);

    }


    /**
     * Inform the user to the error
     */
    private void displayError(int resID) {
        errorPopup = ErrorPopup.newInstance(resID);
        errorPopup.show(getSupportFragmentManager(), "error");
    }

    /**
     * put the views in their original state
     */
    private void initViews() {
        setEthBalance(0.0);
        setErc20Balance(0.0);
        tvAccountEth.setVisibility(View.INVISIBLE);
        tvErc20Balance.setVisibility(View.INVISIBLE);
    }

    private void clearAnimation() {
        checkLoadingState(false);
        animation.clearAnimation();
    }

    /**
     * Check the state of the loading animation
     *
     * @param isLoading true if the request is still loading, false otherwise
     */
    private void checkLoadingState(Boolean isLoading) {
        if (isLoading) {
            animation.setAnimation(R.raw.loading_animation);
            animation.playAnimation();
            tvStateTitle.setText(R.string.loading_in_progress);
            llState.setVisibility(View.VISIBLE);
            llErcToken.setVisibility(View.GONE);
        } else {
            llState.setVisibility(View.GONE);
            llErcToken.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Inform the user to that he lost the internet connexion in a popup
     */
    private void noInternet() {
        errorPopup = ErrorPopup.newInstance(R.string.error_no_internet_connexion);
        errorPopup.show(getSupportFragmentManager(), "no_internet");
        disposables.add(errorPopup.getRefreshObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(v ->
                {
                    errorPopup.dismiss();
                    viewModel.onRefreshClicked();
                })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        errorPopup = null;
    }

}
