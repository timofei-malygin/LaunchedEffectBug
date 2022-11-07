package tm.pets.dialogfragmenttest

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import tm.pets.dialogfragmenttest.sample.StaticMethod
import tm.pets.dialogfragmenttest.sample.TAG

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val appPackage
        get() = InstrumentationRegistry.getInstrumentation().targetContext.packageName

    @Test
    fun noEventsAfterOnSaveInstanceState() = runBlocking {
        StaticMethod.func = {
            Log.d(
                TAG,
                "Consumed index = $it, delta = ${System.currentTimeMillis() - it.time}"
            )
            assertFalse("we don't expect call this after onStop", StaticMethod.isStopped.get())
            assertFalse(
                "we don't expect call this after OnSavedInstanceStateCalled",
                StaticMethod.isOnSaveInstanceStateCalled.get()
            )
        }
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        repeat(10) {
            Log.d("SAMPLE_APP", "started $it")
            waitLauncher(device)
            waitSimpleApp(device)
            delay(10)
        }
    }

    private fun waitLauncher(device: UiDevice) {
        device.pressHome()
        val launcherPackage = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )
    }

    private fun waitSimpleApp(device: UiDevice) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(appPackage)?.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(appPackage).depth(0)),
            LAUNCH_TIMEOUT
        )
    }

}
