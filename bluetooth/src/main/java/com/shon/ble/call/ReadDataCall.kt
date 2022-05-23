package com.shon.ble.call

import android.bluetooth.BluetoothGatt
import com.shon.ble.call.callback.ReadCallback
import java.util.*

class ReadDataCall(
    address: String,
    gatt: BluetoothGatt,
    serviceUUid: UUID,
    characteristicUUID: UUID
) : BaseTransCall<ReadCallback>(address,gatt, serviceUUid, characteristicUUID)