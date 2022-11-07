package tm.pets.dialogfragmenttest.sample

import androidx.compose.runtime.Immutable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@Immutable
data class Event(
    val index: Int,
    val lifecycle: String?,
    val time: Long = System.currentTimeMillis(),
)

class SampleViewModel : ViewModel() {

    val items: StateFlow<Event> = flow {
        var event = Event(0, null)
        while (true) {
            event = Event(event.index + 1, lifecycle?.currentState.toString())
            emit(event)
            delay(20)
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            Event(0, null)
        )

    var lifecycle: Lifecycle? = null
}
