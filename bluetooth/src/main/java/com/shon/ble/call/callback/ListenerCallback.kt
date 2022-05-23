package com.shon.ble.call.callback


abstract class ListenerCallback(val address: String) {
    abstract fun process(mac: String, data: ByteArray): Boolean
}