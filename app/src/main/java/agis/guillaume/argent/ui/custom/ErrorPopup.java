package agis.guillaume.argent.ui.custom;

import agis.guillaume.argent.R;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.lottie.LottieAnimationView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.util.Objects;

import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static com.airbnb.lottie.LottieDrawable.REVERSE;

/**
 * Popup to display an error message
 */
public class ErrorPopup extends DialogFragment {


    @BindView(R.id.llPopupState)
    LinearLayout llState;


    @BindView(R.id.tvPopupStateTitle)
    TextView tvStateTitle;

    @BindView(R.id.tvRefresh)
    TextView tvRefresh;

    @BindView(R.id.tvClose)
    TextView tvClose;

    @BindView(R.id.animation)
    LottieAnimationView animation;

    private static final String MSG_ID = "msg_id";

    private PublishSubject<Boolean> refreshSubject = PublishSubject.create();
    private Observable<Boolean> refreshObservable = refreshSubject.hide();

    public static ErrorPopup newInstance(int msg) {
        ErrorPopup f = new ErrorPopup();
        Bundle args = new Bundle();
        args.putInt(MSG_ID, msg);
        f.setArguments(args);
        return f;
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
     * Listeners on the different buttons
     */
    private void setListeners() {
        tvRefresh.setOnClickListener(__ -> refreshSubject.onNext(true));
        tvClose.setOnClickListener(__ -> dismiss());
    }

    /**
     * Display the error in the view given his resource ID
     *
     * @param msgId ID of the strings to display
     */
    private void displayErrors(int msgId) {
        llState.setVisibility(View.VISIBLE);
        tvStateTitle.setText(msgId);
        animation.setAnimation(R.raw.error_animation);
        animation.playAnimation();
    }


    // display the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom_error, null);
        ButterKnife.bind(this, view);

        int msgId = getArguments() != null ? getArguments().getInt(MSG_ID, 0) : 0;


        initAnimation();
        setListeners();
        displayErrors(msgId);

        builder.setView(view);
        return builder.create();
    }

    public Observable<Boolean> getRefreshObservable() {
        return refreshObservable;
    }
}
