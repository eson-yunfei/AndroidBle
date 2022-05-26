package org.eson.liteble.data

import android.bluetooth.BluetoothGatt
import java.util.*

data class SendCharacteristicsBean(
    val gatt: BluetoothGatt,
    val serviceUUID: UUID,
    val characteristics: UUID
)
