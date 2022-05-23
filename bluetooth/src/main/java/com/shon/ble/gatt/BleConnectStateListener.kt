package com.shon.ble.gatt

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothProfile

interface BleConnectStateListener {

    @SuppressLint("MissingPermission")
    fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {

        if (status == 133) {
            gatt?.disconnect()
            gatt?.close()
            return
        }

        gatt ?: return
        val device = gatt.device

        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                onDeviceConnected(device.address, gatt)
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                gatt.close()
                onDeviceDisconnected(device.address)
            }
        }

    }

    fun onDeviceConnected(address: String, gatt: BluetoothGatt?)

    fun onDeviceDisconnected(address: String)
}