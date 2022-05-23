package com.shon.ble.call

import android.bluetooth.BluetoothGatt
import java.util.*

abstract class BaseTransCall<T>(
    address: String,
    val gatt: BluetoothGatt,
    val serviceUUid: UUID,
    val characteristicUUID: UUID
) : BleCall<T>(address)
