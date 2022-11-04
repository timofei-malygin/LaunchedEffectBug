package tm.pets.dialogfragmenttest.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tm.pets.dialogfragmenttest.R
import java.lang.Thread.sleep

private const val TAG = "SAMPLE_APP"

object StaticMethod {
    @Volatile
    var func: (Int) -> Unit = { action -> Log.d(TAG, "comsume event $action") }

    @Volatile
    var isStopped = false

    @Volatile
    var isOnSaveInstanceStateCalled = false
}

class SampleFragment : BottomSheetDialogFragment() {

    private val vm: SampleViewModel by viewModels()

    @OptIn(ExperimentalLifecycleComposeApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.findViewById<ComposeView>(R.id.compose).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setViewCompositionStrategy()
            setContent {
                val action by vm.items.collectAsStateWithLifecycle(
                    initialValue = 0,
                    viewLifecycleOwner.lifecycle
                )
                LaunchedEffect(action) {
//                    if(!isStateSaved)
                    StaticMethod.func(action)
                }
                Text(text = "action is $action")
            }
        }
        return view
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        trySleep()
        super.onPause()
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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        StaticMethod.isStopped = false
        StaticMethod.isOnSaveInstanceStateCalled = false
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        StaticMethod.isStopped = true
        trySleep()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        StaticMethod.isOnSaveInstanceStateCalled = true
        Log.d(TAG, "onSaveInstanceState")
        trySleep()
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        trySleep()
        super.onDetach()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        trySleep()
        super.onDestroy()
    }
}