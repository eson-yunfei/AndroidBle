package com.shon.ble.call.callback

interface ITransCallBack {
    fun onProcess(data: ByteArray): Boolean
}

