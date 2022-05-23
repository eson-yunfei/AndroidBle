package com.shon.ble.dispatcher

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.shon.ble.call.callback.DiscoverCallback
import com.shon.ble.call.callback.ReadCallback
import com.shon.ble.call.executor.DiscoverExecutor
import com.shon.ble.call.executor.ReadExecutor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal suspend fun executeDiscoverCall(address: String, gatt: BluetoothGatt): Boolean {
    return suspendCoroutine { coroutine ->
        val discoverExecutor = DiscoverExecutor(address, gatt, object : DiscoverCallback() {
            override fun onResult(value: Boolean?) {
                value?.let {
                    coroutine.resume(it)
                }
            }

        })
        discoverExecutor.execute()
    }
}

internal suspend fun executeReadCall(
    address: String,
    gatt: BluetoothGatt,
    gattCharacteristic: BluetoothGattCharacteristic,
    onExecute: () -> Unit = {}
): ByteArray {

    return suspendCoroutine { coroutine ->
        val readExecutor = ReadExecutor(address, gatt, gattCharacteristic, object : ReadCallback {
            override fun onResult(value: ByteArray?) {
                value?.let {
                    coroutine.resume(it)
                }
            }

            override fun onExecute() {
                onExecute.invoke()
            }
        })
        readExecutor.execute()
    }
}