package com.shon.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import com.shon.ble.call.BleCall
import com.shon.ble.dispatcher.BleDispatcher
import com.shon.ble.gatt.BleConnectStateListener
import com.shon.ble.listener.BleDataListener
import com.shon.ble.listener.BleDataListenerExecutor

class BleManager private constructor(val context: Context) {
    private var bluetoothManager: BluetoothManager? = null
    private var dispatcher:BleDispatcher? = null

    private var bleConnectStateListener: BleConnectStateListener? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var bleManager: BleManager? = null
        fun initBleManager(context: Context) {
            bleManager ?: kotlin.run {
                bleManager = BleManager(context)
            }
        }

        fun getManager(): BleManager {
            return bleManager ?: throw NullPointerException("you must init BleManager first..")
        }

    }

    init {
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager?.let {
            dispatcher = BleDispatcher(it)
        }
    }

    internal fun addCall(call:BleCall<*>){
        dispatcher?.addTask(call)
    }

    fun setConnectionStateListener(connectStateListener: BleConnectStateListener) {
        bleConnectStateListener = connectStateListener
    }

    fun getConnectionListener(): BleConnectStateListener? {
        return bleConnectStateListener
    }

    fun setDataListener(bleDataListener: BleDataListener){
        BleDataListenerExecutor.listenerExecutor.setDataListener(bleDataListener)
    }
}