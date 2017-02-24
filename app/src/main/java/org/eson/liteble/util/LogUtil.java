package org.eson.liteble.util;

import android.util.Log;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 17:31
 * @change
 * @chang time
 * @class describe
 */


public class LogUtil {
    private static String TAG = "AndroidBle";

    public LogUtil() {
    }

    public static void e(String msg) {
        Log.e(TAG, getFormatText(msg));
    }

    public static void e(String tag, String msg) {
        Log.e(tag, getFormatText(msg));
    }

    public static void d(String msg) {
        Log.d(TAG, getFormatText(msg));
    }

    public static void i(String msg) {
        Log.i(TAG, getFormatText(msg));
    }

    private static String getFormatText(String msg) {
        return "{ " + msg + " }";
    }
}

