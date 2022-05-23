package com.shon.ble.gatt

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.shon.ble.BleManager
import com.shon.ble.call.data.*
import com.shon.ble.util.BleLog
import com.shon.ble.util.ByteUtil
import org.greenrobot.eventbus.EventBus

internal class BleGattCallback private constructor() : BluetoothGattCallback() {

    companion object {
        val bleGattCallback: BleGattCallback by lazy { BleGattCallback() }
    }


    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        BleLog.d("onConnectionStateChange == status = $status ; newState = $newState")
        BleManager.getManager().getConnectionListener()
            ?.onConnectionStateChange(gatt, status, newState)

        val connectionState = ConnectionState(gatt, status, newState)
        EventBus.getDefault().post(connectionState)
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        val discoverMessage = DiscoverMessage(gatt, status)
        EventBus.getDefault().post(discoverMessage)
    }


    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        val enableNotifyState = EnableNotifyState(gatt, descriptor, status)
        EventBus.getDefault().post(enableNotifyState)
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        val readDataMessage = ReadDataMessage(gatt, characteristic, status)
        EventBus.getDefault().post(readDataMessage)
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        val writeDataMessage = WriteWithoutResult(gatt, characteristic, status)
        EventBus.getDefault().post(writeDataMessage)
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {

        if (gatt == null || characteristic == null) {
            return
        }
        val address = gatt.device.address
        val value = characteristic.value
        BleLog.d("onCharacteristicChanged ======${ByteUtil.getHexString(value)} ")
        val writeResultMessage = WriteResultMessage(address, value)
        EventBus.getDefault().post(writeResultMessage)
    }

    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        gatt ?: return
        val address = gatt.device.address
        val mtuDataMessage = MTUDataMessage(address, mtu, status)
        EventBus.getDefault().post(mtuDataMessage)
    }
}