package org.eson.liteble.ext

import com.shon.ble.call.callback.SendCallback

abstract class WriteDataCallback(private val sendData: String) : SendCallback<Boolean>() {

    override fun onResult(value: Boolean?) {

    }
    override fun needResult(): Boolean {
        return false
    }

    override fun getSendData(): String {
        return sendData
    }

    override fun onProcess(data: ByteArray): ByteArray? {
        return null
    }
}