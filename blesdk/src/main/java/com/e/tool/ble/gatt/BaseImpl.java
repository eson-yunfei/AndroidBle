package com.e.tool.ble.gatt;

import android.os.Handler;
import android.os.Looper;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 16:59
 * Package name : com.e.tool.ble.gatt
 * Des :
 */
class BaseImpl {
    private Handler handler;

    public BaseImpl() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }


    protected void post(Runnable runnable) {
        if (handler == null || runnable == null) {
            return;
        }
        handler.post(runnable);
    }
}
