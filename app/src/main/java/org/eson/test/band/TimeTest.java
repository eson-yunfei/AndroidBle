package org.eson.test.band;

import android.os.SystemClock;

import org.eson.log.LogUtils;

public class TimeTest {
    public static void test(long timeNanos){
        long rxTimestampMillis = System.currentTimeMillis() -
                SystemClock.elapsedRealtime() +
                timeNanos / 1000000;

        LogUtils.d("rxTimestampMillis = "+ rxTimestampMillis);
//        Date rxDate = new Date(rxTimestampMillis);
//        String sDate = new SimpleDateFormat("HH:mm:ss.SSS").format(rxDate);
//        LogUtils.d("sDate = "+ sDate);
    }
}
