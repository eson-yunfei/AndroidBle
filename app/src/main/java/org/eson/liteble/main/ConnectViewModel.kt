package org.eson.liteble.main

import android.bluetooth.BluetoothGatt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shon.ble.data.ConnectResult
import com.shon.ble.util.BleLog
import com.shon.ble.util.ByteUtil
import com.shon.extble.suspendConnectAndDiscoverService
import com.shon.extble.suspendConnectDevice
import com.shon.extble.suspendReadInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.nordicsemi.android.support.v18.scanner.ScanResult

val selectDevice: MutableState<ScanResult?> = mutableStateOf(null)

class ConnectViewModel : ViewModel() {

    val connectedState: MutableState<String> = mutableStateOf("DisConnect")
    val connectedGatt: MutableState<BluetoothGatt?> = mutableStateOf(null)
    val showLogWindow: MutableState<Boolean> = mutableStateOf(false)
    val showSendDataDialog:MutableState<Boolean> = mutableStateOf(false)
    fun startConnectDevice() {

        selectDevice.value?.let {

            viewModelScope.launch {
                val connectResult = suspendConnectAndDiscoverService(it.device.address)
                BleLog.d("connectResult = $connectResult")
                when (connectResult) {
                    is ConnectResult.Connecting -> {
                        connectedState.value = "Connecting"
                    }
                    is ConnectResult.ConnectSuccess -> {
                        connectedState.value = "Connected"
                        connectedGatt.value = connectResult.gatt
                    }
                    is ConnectResult.ConnectError -> {
                        connectedState.value = "Connect Error"
                    }
                    is ConnectResult.Disconnect -> {
                        connectedState.value = "DisConnected"
                    }
                    is ConnectResult.ConnectTimeout -> {
                        connectedState.value = "Connect Timeout"
                    }
                }
            }

        }
    }


    suspend fun readInfo(gatt: BluetoothGatt, serviceUUID: String, characterUUID: String): String? {
        selectDevice.value ?: return null
        selectDevice.value?.let {
            val readInfo = suspendReadInfo(
                it.device.address,
                gatt, serviceUUID, characterUUID
            )
            readInfo?.let { values ->
                return ByteUtil.getHexString(values) + " (" + ByteUtil.byteToCharSequence(values) + ")"
            }

        }
        return null
    }

}




