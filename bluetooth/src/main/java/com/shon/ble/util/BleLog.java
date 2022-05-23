package com.shon.ble.util;

import android.util.Log;

/**
 * Auth : xiao.yunfei
 * Date : 2020/09/27 11:45
 * Package name : com.shon.ble.util
 * Des :
 */
public final class BleLog {

    private static final String TAG = "BleLog";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }
    public static void i(String msg) {
        Log.i(TAG, msg);
    }
    public static void e(String msg) {
        Log.e(TAG, msg);
    }

}
