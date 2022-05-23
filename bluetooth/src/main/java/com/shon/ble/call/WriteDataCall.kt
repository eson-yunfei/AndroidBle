package com.shon.ble.call

import android.bluetooth.BluetoothGatt
import com.shon.ble.call.callback.SendCallback
import java.util.*

class WriteDataCall(
    address: String,
    gatt: BluetoothGatt,
    serviceUUid: UUID,
    characteristicUUID: UUID
) : BaseTransCall<SendCallback<*>>(address,gatt, serviceUUid, characteristicUUID)