package com.shon.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.shon.bluetooth.core.Connect
import com.shon.bluetooth.core.ConnectedDevices
import com.shon.bluetooth.core.Device
import com.shon.bluetooth.util.BleLog
import java.util.*

/**
 * Auth : xiao.yunfei
 * Date : 2020/10/06 20:19
 * Package name : com.shon.bluetooth
 * Des :  设备连接分发器
 */
class ConnectDispatcher internal constructor() {
    val connectedDevices: ConnectedDevices = ConnectedDevices()
    private val connectList: ArrayList<Connect> = ArrayList()
    private val handler: Handler = Handler(Looper.getMainLooper())

    /**
     * 设备连接异常
     *
     * @param address
     * @param status
     */
    fun onDeviceError(address: String?, status: Int) {
        handler.post { connectedDevices.onDeviceConnectError(address!!, status) }
        connect ?: return
        if (!TextUtils.equals(address, connect!!.address)) return
        if (connect!!.reTryTimes <= 0) {
            handler.post { connect!!.connectCallback.onConnectError(address, status) }
            startNextConnect(true)
            return
        }
        connect!!.reTryTimes = connect!!.reTryTimes - 1
        connectDevice()
    }

    /**
     * 设备断开连接
     *
     * @param bluetoothDevice
     * @param gatt
     */
    fun onDeviceDisConnected(bluetoothDevice: BluetoothDevice?, gatt: BluetoothGatt) {
        BleLog.d("onDeviceDisConnected  ")
        handler.post { connectedDevices.onDeviceDisConnect(bluetoothDevice!!, gatt) }
        connect?.let {
            handler.post {
                BleLog.d("设备断开连接，回调给正在连接中的设备")
                it.connectCallback?.onDisconnected(bluetoothDevice?.address)
                connectList.remove(it)
                startNextConnect(true)
            }
        }
        connectList.find {
            TextUtils.equals(it.address, bluetoothDevice!!.address)
        }?.let {
            handler.post {
                BleLog.d("设备断开连接，回调给等待连接中的设备")
                it.connectCallback?.onDisconnected(bluetoothDevice?.address)
                connectList.remove(it)
            }
        }

    }

    /**
     * 设备连接成功
     *
     * @param bluetoothDevice
     * @param gatt
     */
    fun onDeviceConnected(bluetoothDevice: BluetoothDevice?, gatt: BluetoothGatt) {
        val address = bluetoothDevice!!.address
        var device = connectedDevices.getDevice(address)
        if (device != null) {
            handler.post { device!!.connect!!.connectCallback?.onConnectSuccess(address, gatt) }
            startNextConnect(true)
            gatt.discoverServices()
            return
        }
        BleLog.d("已连接设备列表中 不存在该设备信息，准备添加信息")
        if (connect != null && TextUtils.equals(address, connect!!.address)) {
            handler.post { connect!!.connectCallback?.onConnectSuccess(address, gatt) }
            device = Device()
            device.gatt = gatt
            device.connect = connect
            device.deviceAddress = address
            device.connected = true
            device.deviceName = bluetoothDevice.name
            connectedDevices.addDevice(device)
            BleLog.d("连接设备列表添加信息成功")
            gatt.discoverServices()
        }
        startNextConnect(true)
    }

    /**
     * 设备服务被 启用
     *
     * @param address
     */
    fun onServicesDiscovered(address: String?) {
        handler.post { connectedDevices.onServicesDiscovered(address!!) }
    }

    fun enqueue(connect: Connect) {
        connectList.add(0, connect)
        startNextConnect(false)
    }

    private var connect: Connect? = null

    @Synchronized
    private fun startNextConnect(finish: Boolean) {
        if (finish) {
            if (connect != null) {
                connectList.remove(connect)
                connect = null
            }
        }
        if (connect != null || connectList.isEmpty()) return

        connect = connectList[connectList.size - 1]
        connectDevice()
    }

    private fun connectDevice() {
        val device = connectedDevices.getDevice(connect!!.address)
        if (device != null && device.connected) {
            BleLog.d("该设备已连接")
            connect!!.connectCallback.onConnected()
            startNextConnect(true)
        } else {
            BleLog.d("开始新的设备连接 ： " + connect!!.address)
            val adapter = BLEManager.getInstance().manager.adapter ?: return
            val remoteDevice = adapter.getRemoteDevice(connect!!.address)
            val context = BLEManager.getInstance().context
            if (context == null || remoteDevice == null) {
                return
            }
            connect!!.connect(context, remoteDevice, BLEManager.getInstance().gattCallback)
        }
    }

    @Synchronized
    fun disconnect(mac: String?) {
        connectedDevices.getDevice(mac!!)?.let { device ->
            device.gatt?.let { gatt ->
                gatt.disconnect()
                refreshCache(gatt)
                gatt.close()
            }
        }

    }

    private fun refreshCache(gatt: BluetoothGatt) {
        try {
            val localMethod = gatt.javaClass.getMethod("refresh") ?: return
            localMethod.invoke(gatt)
        } catch (var3: Exception) {
            BleLog.e("An exception occured while refreshing device")
        }
    }

}