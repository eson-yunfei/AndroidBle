package com.shon.ble.call.executor

import org.greenrobot.eventbus.EventBus

abstract class BaseExecutor<T, M> constructor(val callback: T) {

    open fun execute(){
        EventBus.getDefault().register(this)
    }

    open fun onReceiverMessage(message: M) {

        EventBus.getDefault().unregister(this)
    }

}