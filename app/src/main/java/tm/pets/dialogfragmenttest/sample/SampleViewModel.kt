package tm.pets.dialogfragmenttest.sample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class SampleViewModel: ViewModel() {

    val items = flow {
        var index = 0
        while(true){
            emit(index)
            index++
            delay(20)
        }
    }
}