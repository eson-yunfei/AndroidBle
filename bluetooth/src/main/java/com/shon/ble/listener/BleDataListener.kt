package com.shon.ble.listener

interface BleDataListener {
    fun onReceiverData(address: String, value: ByteArray)
}