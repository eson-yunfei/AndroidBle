package com.shon.extble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.shon.ble.call.*
import com.shon.ble.call.callback.*
import com.shon.ble.data.ConnectResult
import com.shon.ble.util.BleLog
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun suspendConnectDevice(address: String): ConnectResult {
    return suspendCoroutine { coroutine ->
        ConnectorCall(address).enqueue(object : ConnectCallback() {
            override fun onResult(value: ConnectResult?) {
                BleLog.d("suspendConnectDevice onResult : value = $value")
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

suspend fun suspendEnableNotification(
    address: String, gatt: BluetoothGatt, serviceUUid: UUID,
    characteristic: UUID
): Boolean {
    return suspendCoroutine { coroutine ->
        EnableNotifyCall(address, gatt, serviceUUid, characteristic).enqueue(object :
            EnableNotifyCallback {
            override fun onResult(value: Boolean?) {
                BleLog.d("EnableNotifyCall onResult value = $value")
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

            override fun onResult(value: ByteArray?) {
                coroutine.resume(value)
            }

            override fun onExecuted() {
                execute.invoke()
            }

        })
    }
}


suspend fun suspendWriteWithoutCallback(
    address: String, bluetoothGatt: BluetoothGatt, serviceUUid: String,
    characteristic: String, sendData: String,
    execute: () -> Unit = {}
): Boolean {
    return suspendCoroutine { coroutine ->
        WriteDataCall(
            address, bluetoothGatt, UUID.fromString(serviceUUid),
            UUID.fromString(characteristic)
        ).enqueue(object : SendCallback<Boolean>() {
            override fun onResult(value: Boolean?) {

            }
            override fun onSendResult(sendResult: Boolean) {
                BleLog.d("suspendWriteWithoutCallback : sendResult = $sendResult")
                coroutine.resume(sendResult)
            }
            override fun onExecuted() {
                execute.invoke()
            }

            override fun needResult(): Boolean {
                return false
            }

            override fun getSendData(): String {
                return sendData
            }

            override fun onProcess(data: ByteArray): ByteArray? {
                return null
            }

        })
    }
}