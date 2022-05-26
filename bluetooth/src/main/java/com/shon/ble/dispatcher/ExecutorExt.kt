package com.shon.ble.dispatcher

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.shon.ble.call.callback.*
import com.shon.ble.call.callback.WriteCallback
import com.shon.ble.call.executor.DiscoverExecutor
import com.shon.ble.call.executor.EnableNotifyExecutor
import com.shon.ble.call.executor.ReadExecutor
import com.shon.ble.call.executor.WriterExecutor
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

internal suspend fun executeEnableNotification(
    address: String, gatt: BluetoothGatt, descriptor: String,
    gattCharacteristic: BluetoothGattCharacteristic
): Boolean {
    return suspendCoroutine { coroutine ->
        val enableNotifyExecutor = EnableNotifyExecutor(
            address,
            gatt,
            descriptor,
            gattCharacteristic,
            object : EnableNotifyCallback {
                override fun onResult(value: Boolean?) {
                    value?.let {
                        coroutine.resume(it)
                    }
                }

            })
        enableNotifyExecutor.execute()
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

            override fun onExecuted() {
                onExecute.invoke()
            }
        })
        readExecutor.execute()
    }
}

internal suspend fun <T> executeWriteCall(
    address: String,
    gatt: BluetoothGatt,
    gattCharacteristic: BluetoothGattCharacteristic,
    sendDataCallback: SendCallback<T>
): Boolean {

    return suspendCoroutine { coroutine ->
        val writerExecutor = WriterExecutor(address,
            gatt, gattCharacteristic, sendDataCallback, object : WriteCallback() {
                override fun onResult(value: Boolean?) {
                    value?.let {
                        coroutine.resume(it)
                        sendDataCallback.onSendResult(true)
                    }
                }
                override fun onExecuted() {
                    sendDataCallback.onExecuted()
                }
            })

        writerExecutor.execute()
    }

}