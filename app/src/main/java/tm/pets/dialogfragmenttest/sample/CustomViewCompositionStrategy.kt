package tm.pets.dialogfragmenttest.sample

import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

class CustomViewCompositionStrategy(private val lifecycle: Lifecycle) : ViewCompositionStrategy {

    override fun installFor(view: AbstractComposeView): () -> Unit {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                view.disposeComposition()
            }
        }
        lifecycle.addObserver(observer)
        return { lifecycle.removeObserver(observer) }
    }
}

