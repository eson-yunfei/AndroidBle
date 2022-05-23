package com.shon.ble.call

import com.shon.ble.BleManager


abstract class BleCall<T>(val address: String) {

    private var mCallback: T? = null

    fun getCallBack(): T {
        return mCallback!!
    }

    fun enqueue(callback: T) {
        mCallback = callback
        BleManager.getManager().addCall(this)
    }
}