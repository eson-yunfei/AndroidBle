package com.shon.bluetooth.core.gatt

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothProfile
import com.shon.bluetooth.ConnectDispatcher
import com.shon.bluetooth.util.BleLog

/**
 * Auth : xiao.yunfei
 * Date : 2020/9/28 16:23
 * Package name : com.shon.bluetooth.core.gatt
 * Des :
 */
class ConnectionGattCallback(private val connectDispatcher: ConnectDispatcher) {


    fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        BleLog.d(" onConnectionStateChange  status = $status ; newState = $newState")

        val device = gatt.device

        BleLog.d("  device name  = " + device.name + " ; mac = " + device.address)
        if (status != BluetoothGatt.GATT_SUCCESS){
            gatt.close()
            connectDispatcher.onDeviceError(device.address,status)
            return
        }
        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
//                gatt.discoverServices()
                connectDispatcher.onDeviceConnected(device, gatt)
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                gatt.close()
                connectDispatcher.onDeviceDisConnected(device, gatt)
            }
            BluetoothProfile.STATE_CONNECTING -> {

            }
        }

    }


    fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        val device = gatt.device
        connectDispatcher.onServicesDiscovered(device.address)

    }

}