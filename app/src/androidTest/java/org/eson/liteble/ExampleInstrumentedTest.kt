package org.eson.liteble

import android.os.SystemClock
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.eson.log.LogUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        LogUtils.d("11111111111111")
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("org.eson.liteble.test", appContext.packageName)

        test()

    }


    private fun test() {
        val rxTimestampMillis = System.currentTimeMillis() -
                SystemClock.elapsedRealtime() +
                233821638290418L / 1000000
        println("rxTimestampMillis = $rxTimestampMillis")
        val rxDate = Date(rxTimestampMillis)
        val sDate = SimpleDateFormat("HH:mm:ss.SSS").format(rxDate)
        println("sDate = $sDate")
    }
}