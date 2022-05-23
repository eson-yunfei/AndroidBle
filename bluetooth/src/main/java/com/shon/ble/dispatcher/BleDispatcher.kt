package com.shon.ble.dispatcher

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import com.shon.ble.util.BleLog
import com.shon.ble.call.*
import com.shon.ble.call.callback.ReadCallback
import com.shon.ble.call.callback.SendCallback
import com.shon.ble.call.executor.*
import com.shon.ble.getGattCharacteristic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class BleDispatcher internal constructor(private val manager: BluetoothManager) {
    private val channel = Channel<BleCall<*>>(Channel.Factory.UNLIMITED)

    init {
        startCall()
    }


    /**
     * 添加连接任务
     */
    fun addTask(call: BleCall<*>) {
        CoroutineScope(Dispatchers.IO).launch {
            channel.send(call)
        }
    }

    private fun startCall() {

        CoroutineScope(Dispatchers.IO).launch {

            while (true) {
                val call = channel.receive()
                val address = call.address
                when (call) {
                    is ConnectorCall -> {
                        val connectCallback = call.getCallBack()
                        val async =
                            async { ConnExecutor(address, manager, connectCallback).execute() }
                        connectCallback.onResult(async.await())
                    }
                    is MTUCall -> {
                        val execute = MTUExecutor(address, call.gatt, call.mtu).execute()
                        call.getCallBack().onResult(execute)
                    }
                    is DiscoverCall -> discoverService(call)
                    is EnableNotifyCall -> enableNotifyService(call)
                    is ReadDataCall -> readData(call)
                    is WriteDataCall -> {
                        val async = async { writeData(call) }
                        val await = async.await()
                        BleLog.d("write await  = $await")
                    }
                }
            }
        }
    }

    private suspend fun enableNotifyService(call: EnableNotifyCall) {
        val gattCharacteristic = call.getCharacteristic()
        val execute = EnableNotifyExecutor(
            call.gatt,
            call.address,
            call.descriptor,
            gattCharacteristic
        ).execute()
        call.getCallBack().onResult(execute)
    }


    private suspend fun discoverService(call: DiscoverCall) {
        val address = call.address
        val gatt = call.gatt
        val discoverResult = executeDiscoverCall(address, gatt)
        call.getCallBack().onResult(discoverResult)
    }


    private suspend fun readData(call: ReadDataCall) {
        val gattCharacteristic = call.getCharacteristic()
        val callBack = call.getCallBack()
        gattCharacteristic ?: return
        val executeReadCallResult = executeReadCall(call.address, call.gatt, gattCharacteristic) {
            callBack.onExecute()
        }
        callBack.onResult(executeReadCallResult)
    }

    private suspend fun writeData(call: WriteDataCall): Boolean? {
        val characteristic = call.getCharacteristic()
        val callBack: SendCallback<Any> = call.getCallBack() as SendCallback<Any>
        return WriterExecutor(call.address, call.gatt, characteristic, callBack)
            .execute()
    }

    private fun BaseTransCall<*>.getCharacteristic(): BluetoothGattCharacteristic? {
        return getGattCharacteristic(gatt, serviceUUid, characteristicUUID)
    }
}