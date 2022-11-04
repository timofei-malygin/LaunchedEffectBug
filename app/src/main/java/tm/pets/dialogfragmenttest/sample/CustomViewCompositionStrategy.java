package tm.pets.dialogfragmenttest.sample;

import androidx.annotation.NonNull;
import androidx.compose.ui.platform.AbstractComposeView;
import androidx.compose.ui.platform.ViewCompositionStrategy;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class CustomViewCompositionStrategy implements ViewCompositionStrategy {

    private final Lifecycle lifecycle;

    public CustomViewCompositionStrategy(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @NonNull
    @Override
    public Function0<Unit> installFor(@NonNull final AbstractComposeView view) {
        final LifecycleEventObserver observer = (source, event) -> {
            if (event == Lifecycle.Event.ON_STOP) {
                view.disposeComposition();
            }
        };

        lifecycle.addObserver(observer);
        return () -> {
            lifecycle.removeObserver(observer);
            return Unit.INSTANCE;
        };
    }
}
