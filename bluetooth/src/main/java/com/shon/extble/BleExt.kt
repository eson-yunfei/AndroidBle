package com.shon.extble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.shon.ble.call.ConnectorCall
import com.shon.ble.call.DiscoverCall
import com.shon.ble.call.ReadDataCall
import com.shon.ble.call.callback.ConnectCallback
import com.shon.ble.call.callback.DiscoverCallback
import com.shon.ble.call.callback.ReadCallback
import com.shon.ble.data.ConnectResult
import com.shon.ble.util.BleLog
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun suspendConnectDevice(address: String): ConnectResult {
    return suspendCoroutine { coroutine ->
        ConnectorCall(address).enqueue(object : ConnectCallback() {
            override fun onResult(value: ConnectResult?) {
                value?.let {
                    coroutine.resume(it)
                }
            }
        })
    }
}

@SuppressLint("MissingPermission")
suspend fun suspendConnectAndDiscoverService(address: String): ConnectResult {
    val connectResult = suspendConnectDevice(address)
    return if (connectResult is ConnectResult.ConnectSuccess) {

        val discoverService = suspendDiscoverService(address, connectResult.gatt)
        BleLog.d("discoverService  =  $discoverService")
        if (discoverService) {
            connectResult
        } else {
            connectResult.gatt.apply {
                disconnect()
                connect()
            }
            ConnectResult.ConnectError(address, -1)
        }
    } else {
        connectResult
    }
}

suspend fun suspendDiscoverService(address: String, gatt: BluetoothGatt): Boolean {
    return suspendCoroutine { coroutine ->
        DiscoverCall(address, gatt).enqueue(object : DiscoverCallback() {
            override fun onResult(value: Boolean?) {
                coroutine.resume(value!!)
            }
        })
    }
}

suspend fun suspendReadInfo(
    address: String, bluetoothGatt: BluetoothGatt, serviceUUid: String,
    characteristic: String, execute: () -> Unit = {}
): ByteArray? {
    return suspendCoroutine { coroutine ->

        ReadDataCall(
            address, bluetoothGatt, UUID.fromString(serviceUUid),
            UUID.fromString(characteristic)
        ).enqueue(object : ReadCallback {
            override fun onExecute() {
                execute.invoke()
            }

            override fun onResult(value: ByteArray?) {
                coroutine.resume(value)
            }

        })
    }
}