package com.shon.ble.call.data

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor

internal data class EnableNotifyState(val gatt: BluetoothGatt?,
                             val descriptor: BluetoothGattDescriptor?,
                             val status: Int)