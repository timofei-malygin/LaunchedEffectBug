package tm.pets.dialogfragmenttest.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import tm.pets.dialogfragmenttest.R
import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicBoolean

const val TAG = "SAMPLE_APP"

object StaticMethod {
    @Volatile
    var func: (Event) -> Unit = { event ->
        Log.d(
            TAG,
            "Consumed index = $event, delta = ${System.currentTimeMillis() - event.time}"
        )
    }

    val isStopped = AtomicBoolean(false)
    val isOnSaveInstanceStateCalled = AtomicBoolean(false)
    var brokenCase = true
}

class SampleFragment : Fragment() {

    private val vm: SampleViewModel by viewModels()

    @OptIn(ExperimentalLifecycleComposeApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.lifecycle = viewLifecycleOwner.lifecycle
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.findViewById<ComposeView>(R.id.compose).apply {
            // tried any of ViewCompositionStrategy
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
//            setViewCompositionStrategy(CustomViewCompositionStrategy(viewLifecycleOwner.lifecycle))
            setContent {
                val event: Event by vm.items.collectAsStateWithLifecycle(
                    viewLifecycleOwner.lifecycle
                )
                if (StaticMethod.brokenCase) {
                    LaunchedEffect(event) {
                        Log.d(
                            TAG,
                            viewLifecycleOwner.lifecycle.currentState.toString() + " - " + isStateSaved + " " + isActive
                        )
                        StaticMethod.func(event)
                    }
                } else {
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(key1 = Unit) {
                        scope.launch {
                            vm
                                .items
                                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                                .collect { event ->
                                    Log.d(
                                        TAG,
                                        viewLifecycleOwner.lifecycle.currentState.toString() + " - " + isStateSaved + " " + isActive
                                    )
                                    StaticMethod.func(event)
                                }
                        }
                    }

                }
                Text(text = "action is $event")
            }
        }
        return view
    }

    private fun trySleep() {
        val ts = System.currentTimeMillis()
        while (System.currentTimeMillis() - ts < 30) {
            try {
                sleep(30)
            } catch (_: Exception) {
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored")
        StaticMethod.isOnSaveInstanceStateCalled.set(false)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        StaticMethod.isStopped.set(false)
    }

    override fun onPause() {
        trySleep()
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
//        trySleep()
        Log.d(TAG, "onStop")
        super.onStop()
        StaticMethod.isStopped.set(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
//        trySleep()
        Log.d(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        StaticMethod.isOnSaveInstanceStateCalled.set(true)
    }

    override fun onDetach() {
        trySleep()
        Log.d(TAG, "onDetach")
        super.onDetach()
    }

    override fun onDestroy() {
        trySleep()
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }
}
