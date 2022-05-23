package com.shon.ble.call

import android.bluetooth.BluetoothGatt
import com.shon.ble.call.callback.EnableNotifyCallback
import java.util.*

class EnableNotifyCall constructor(
    address: String,
    gatt: BluetoothGatt,
    serviceUUID: UUID,
    characteristicUUID: UUID,
    val descriptor: String = "00002902-0000-1000-8000-00805F9B34FB",
) : BaseTransCall<EnableNotifyCallback>(address, gatt, serviceUUID, characteristicUUID)