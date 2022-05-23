package com.shon.ble.call.data

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

internal data class ReadDataMessage(
    val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?,
    val status: Int
)

abstract class WriteDataMessage(val value: Int)

internal data class WriteWithoutResult(
    val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?,
    val status: Int
) : WriteDataMessage(status)

data class WriteResultMessage(
    val address: String,
    val data: ByteArray,
    val status: Int = 10000
) : WriteDataMessage(status) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WriteResultMessage

        if (address != other.address) return false
        if (!data.contentEquals(other.data)) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + status
        return result
    }
}

internal data class MTUDataMessage(
    val address: String,
    val mtu: Int,
    val status: Int = 10000
)