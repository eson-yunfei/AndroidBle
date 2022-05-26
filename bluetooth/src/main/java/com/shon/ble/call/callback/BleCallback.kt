package com.shon.ble.call.callback

interface BleCallback<T> {
    fun onResult(value: T?)
    fun timeout(): Long = 20_000
    fun onExecuted()
}