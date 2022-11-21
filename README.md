# LaunchedEffectBug
The project helps to get crash when we call something after onSavedInstanceState while using LaunchedEffect.

## The problem
We need to collect data and proceed it only when Fragment in state onStart (e.x. call dialog#dismiss()). For this purpose LauchedEffect was used based on documentation: https://developer.android.com/jetpack/compose/side-effects#launchedeffect. 
## Steps
- start the app via icon or restore from recent applications
- press home button
- repeat several times

## Expected
- the app will hide and open without a crash

## Actual
- crashed because we call something after onSavedInstanceState.

## Additional information and logs
The application logs the lifecycle state and an event  

lifecycle state looks like: 
```
viewLifecycleOwner.lifecycle.currentState.toString() + " - " + Fragment.isStateSaved + " " + CoroutineScope.isActive
```
An event state looks like:
```
Consumed index = $event, delta = ${System.currentTimeMillis() - event.time}
```
the delta line is different between time when event was created and time when it was logged.
The event class
```
data class Event(
    val index: Int,
    val lifecycle: String?,
    val time: Long = System.currentTimeMillis(),
)
```

The log before crash:
```
11-21 16:40:02.771  6368  6368 D SAMPLE_APP: onStop
11-21 16:40:02.771  6368  6368 D SAMPLE_APP: onSaveInstanceState
11-21 16:40:02.771  6368  6368 D SAMPLE_APP: CREATED - true true // lifecycle state is CREATED! but state is only onSaveInstanceState!. isStateSaved returned true! and coroutineScope is active
11-21 16:40:02.772  6368  6368 D SAMPLE_APP: Consumed index = Event(index=64, lifecycle=STARTED, time=1669045202764), delta = 8
```
