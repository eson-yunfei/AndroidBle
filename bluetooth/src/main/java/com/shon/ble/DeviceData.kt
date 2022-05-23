package com.shon.ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.text.TextUtils
import com.shon.ble.util.BleLog
import java.util.*

internal data class ConnectedDevice(val address: String, val gatt: BluetoothGatt)

private val connectingDeviceList: MutableList<ConnectedDevice> = mutableListOf()

@Synchronized
internal fun addConnectDevice(address: String, gatt: BluetoothGatt?) {
    gatt ?: return
    val findDevice = getConnectedDevice(address)
    if (findDevice == null) {
        connectingDeviceList.add(ConnectedDevice(address, gatt))
    }
}

internal fun getConnectGatt(address: String): BluetoothGatt? {
    val findDevice = getConnectedDevice(address)
    return findDevice?.gatt
}


internal fun getGattCharacteristic(
    gatt: BluetoothGatt?,
    serviceUUid: UUID,
    characteristicUUID: UUID
): BluetoothGattCharacteristic? {
    gatt ?: return null
    val device = gatt.device
    device ?: return null
    val service = gatt.getService(serviceUUid)
    BleLog.d("service = $service")
    service ?: return null
    return service.getCharacteristic(characteristicUUID)
}

private fun getConnectedDevice(address: String): ConnectedDevice? {
    return connectingDeviceList.find {
        TextUtils.equals(it.address, address)
    }
}
//
//private var listenerCallList: MutableList<ListenerCall> = mutableListOf()
//
//internal fun addListenerCall(listenerCall: ListenerCall) {
//    listenerCallList.add(listenerCall)
//}
//
//internal fun processDataByListener(address: String, value: ByteArray): Boolean {
//    val onProcess = listenerCallList.find {
//        TextUtils.equals(it.address, address)
//    }?.getCallBack()?.process(address, value) ?: false
//    return onProcess
//}