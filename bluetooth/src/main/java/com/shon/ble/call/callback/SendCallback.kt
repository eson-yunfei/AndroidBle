package com.shon.ble.call.callback

abstract class SendCallback<T> : BleCallback<T> {
    abstract fun getSendData(): String

    open fun receiveFinish(): Boolean = true

    open fun needResult(): Boolean = true

    abstract fun onProcess(data: ByteArray): ByteArray?

    open fun convertResult(result: ByteArray): T? {
        return null
    }

    open fun onSendResult(sendResult: Boolean) {}
}