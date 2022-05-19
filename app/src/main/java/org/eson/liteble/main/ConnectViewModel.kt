package org.eson.liteble.main

import android.bluetooth.BluetoothGatt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shon.bluetooth.core.Connect
import com.shon.bluetooth.core.ConnectCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult

val selectDevice: MutableState<ScanResult?> = mutableStateOf(null)

class ConnectViewModel : ViewModel() {

    val connectedState: MutableState<String> = mutableStateOf("DisConnect")
    val connectedGatt: MutableState<BluetoothGatt?> = mutableStateOf(null)
    fun startConnectDevice() {

        selectDevice.value?.let {
            Connect(it.device.address).enqueue(object : ConnectCallback() {
                override fun onConnectSuccess(address: String?, gatt: BluetoothGatt?) {
                    connectedState.value = "Connected"
                }

                override fun onConnectError(address: String?, errorCode: Int) {
                    connectedState.value = "Connect Error"
                }

                override fun onServiceEnable(address: String?, gatt: BluetoothGatt?) {
                    connectedGatt.value = gatt
                }

                override fun onDisconnected(address: String?) {
                    connectedState.value = "DisConnect"
                }

            })
        }
    }


    private fun loadBleGattInfo(gatt: BluetoothGatt) {

    }

}