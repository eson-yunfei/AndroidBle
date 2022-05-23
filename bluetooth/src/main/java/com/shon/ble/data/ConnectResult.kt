package com.shon.ble.data

import android.bluetooth.BluetoothGatt

sealed class ConnectResult {
    data class ConnectSuccess(val address: String, val gatt: BluetoothGatt) : ConnectResult()
    data class Disconnect(val address: String) : ConnectResult()
    data class ConnectTimeout(val address: String) : ConnectResult()
    data class ConnectError(val address: String, val errorCode: Int) : ConnectResult()

    data class Connecting(val address: String):ConnectResult()
}