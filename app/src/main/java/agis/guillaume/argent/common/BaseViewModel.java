package agis.guillaume.argent.common;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.ViewModel;
import com.jakewharton.rxrelay2.BehaviorRelay;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Base ViewModel to manage the disposables, using MVI architecture.
 */
public class BaseViewModel<T> extends ViewModel implements DefaultLifecycleObserver {

    protected CompositeDisposable disposables = new CompositeDisposable();
    private BehaviorRelay<T> viewStateRelay = BehaviorRelay.create();
    private Observable<T> observableState = viewStateRelay.hide();

    public Observable<T> getViewStateObservable() {
        return observableState;
    }


    protected void emitViewState(T viewState) {
        viewStateRelay.accept(viewState);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
