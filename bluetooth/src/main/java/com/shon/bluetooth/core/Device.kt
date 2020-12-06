package com.shon.bluetooth.core

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.shon.bluetooth.util.BleLog

/**
 * Auth : xiao.yunfei
 * Date : 2020/10/06 20:21
 * Package name : com.shon.bluetooth.contorller.bean
 * Des : 已连接的设备 管理
 *
 */

class ConnectedDevices {
    private val devices: MutableList<Device> = mutableListOf()

    fun getDevice(address: String): Device? {
        return devices.find {
            it.deviceAddress == address
        }
    }

    fun onDeviceConnectError(address: String, status: Int) {
        getDevice(address)?.let {
            it.connect?.connectCallback?.onConnectError(address, status)
        }
    }

    fun onDeviceDisConnect(bluetoothDevice: BluetoothDevice, gatt: BluetoothGatt) {
        val address = bluetoothDevice.address
        getDevice(address)?.let {
            BleLog.d("设备断开连接，回调给已存在列表中的设备")
            it.connect?.connectCallback?.onDisconnected(address)
            devices.remove(it)
        }
    }

    fun onServicesDiscovered(address: String) {
        getDevice(address)?.let {

            BleLog.d("准备回调给用户 设备 服务已开启")
            it.connect?.connectCallback?.onServiceEnable(address, it.gatt)
        }
    }

    fun addDevice(device: Device) {
        devices.add(device)
    }

}

/**
 * 已连接的设备
 */
data class Device(
    var deviceName: String? = null,
    var deviceAddress: String? = null,
    var serviceEnable: Boolean = false,  //服务 是否被 启用
    var connected: Boolean = false,  //连接状态
    var gatt: BluetoothGatt? = null,  //
    var connect: Connect? = null  //
)