package com.shon.ble.ext

import android.bluetooth.BluetoothGatt
import com.shon.ble.util.BleLog
import com.shon.ble.gatt.BleGattCallback
import com.shon.ble.gatt.callback.DiscoverServiceCallback
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * 启用服务
 */
suspend fun BluetoothGatt.discoverService(address: String): Boolean = suspendCoroutine {
    BleGattCallback.bleGattCallback.discoverService(this,
        object : DiscoverServiceCallback(address) {
            override fun discovered() {
                it.resume(true)
                BleLog.i("device $address service discovered")
                BleGattCallback.bleGattCallback.clearDiscoverCallback()
            }
        })
}