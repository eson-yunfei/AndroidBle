package com.shon.ble.call.executor

import com.shon.ble.call.callback.BleCallback
import org.greenrobot.eventbus.EventBus

abstract class BaseExecutor<T> constructor(val callback: BleCallback<*>) {

    open fun execute(){
        EventBus.getDefault().register(this)
        callback.onExecuted()
    }

    open fun onReceiverMessage(message: T) {

        unregisterReceiver()
    }

    fun unregisterReceiver(){
        EventBus.getDefault().unregister(this)
    }
}