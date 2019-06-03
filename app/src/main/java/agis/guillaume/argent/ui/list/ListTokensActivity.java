package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.R;
import agis.guillaume.argent.common.BaseActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.airbnb.lottie.LottieAnimationView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static com.airbnb.lottie.LottieDrawable.REVERSE;

/**
 * View associated to the list of ERC 20 tokens with their balance
 */
public class ListTokensActivity extends BaseActivity {

    @Inject
    ListTokensViewModel.Factory viewModelFactory;

    @Inject
    ListTokensAdapter tokenAdapter;

    @BindView(R.id.animation)
    LottieAnimationView animation;

    @BindView(R.id.recyclerTokenView)
    RecyclerView recyclerTokenView;

    @BindView(R.id.llState)
    LinearLayout llState;


    @BindView(R.id.tvStateTitle)
    TextView tvStateTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tokens);
        init();
        initViewModel();
        activateBackButton(toolbar);
    }

    /**
     * Init the view
     */
    private void init() {
        initAnimation();
        initRecyclerView();
        initViews();
    }


    /**
     * Init animation of the comment button to push the user to click on it
     */
    private void initAnimation() {
        // set animation
        animation.setRepeatMode(REVERSE);
        animation.setRepeatCount(INFINITE);
    }

    /**
     * Init recycler view
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTokenView.setLayoutManager(layoutManager);
        recyclerTokenView.setItemAnimator(new DefaultItemAnimator());
        recyclerTokenView.setAdapter(tokenAdapter);
        recyclerTokenView.setHasFixedSize(true);
    }

    /**
     * init viewmodel
     */
    private void initViewModel() {
        ListTokensViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListTokensViewModel.class);
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
    private void render(ListTokensViewState viewState) {
        initViews();

        if (viewState instanceof ListTokensViewState.ShowLoading) {
            checkLoadingState(true);
        } else if (viewState instanceof ListTokensViewState.DisplayTokensList) {
            ListTokensViewState.DisplayTokensList state = (ListTokensViewState.DisplayTokensList) viewState;
            tokenAdapter.update(state.getList());
        } else if (viewState instanceof ListTokensViewState.DisplayEmptyListMessage) {
            displayEmptyListMessage();
        } else if (viewState instanceof ListTokensViewState.Error) {
            displayError();
        } else if (viewState instanceof ListTokensViewState.NoInternet) {
            noInternet();
        }
    }

    /**
     * Inform the user to that the list of posts is empty
     */
    private void displayEmptyListMessage() {
        llState.setVisibility(View.VISIBLE);
        animation.setAnimation(R.raw.empty_list_animation);
        tvStateTitle.setText(R.string.error_no_token_available);
        animation.playAnimation();
        hideRecyclerView();
    }


    /**
     * Inform the user to the error
     */
    private void displayError() {
        llState.setVisibility(View.VISIBLE);
        animation.setAnimation(R.raw.error_animation);
        tvStateTitle.setText(R.string.error_try_again_later);
        animation.playAnimation();
        hideRecyclerView();
    }

    /**
     * put the views in their original state
     */
    private void initViews() {
        checkLoadingState(false);
        animation.clearAnimation();
        recyclerTokenView.setVisibility(View.VISIBLE);
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
            hideRecyclerView();
        } else {
            llState.setVisibility(View.GONE);
        }
    }

    /**
     * Inform the user to that he lost the internet connexion
     */
    private void noInternet() {
        llState.setVisibility(View.VISIBLE);
        tvStateTitle.setText(R.string.error_no_internet_connexion);
        animation.setAnimation(R.raw.error_animation);
        animation.playAnimation();
        hideRecyclerView();
    }

    /**
     * hide recycler view
     */
    private void hideRecyclerView() {
        recyclerTokenView.setVisibility(View.INVISIBLE);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ListTokensActivity.class);
        context.startActivity(starter);
    }
}
