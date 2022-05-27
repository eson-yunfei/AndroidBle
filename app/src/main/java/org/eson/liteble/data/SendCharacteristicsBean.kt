package org.eson.liteble.data

import android.bluetooth.BluetoothGatt
import java.util.*

data class SendCharacteristicsBean(
    val gatt: BluetoothGatt,
    val serviceUUID: String,
    val characteristics: String
)
