package com.shon.ble.call.data

import android.bluetooth.BluetoothGatt


internal data class ConnectionState(val gatt: BluetoothGatt?, val status: Int, val newState: Int)