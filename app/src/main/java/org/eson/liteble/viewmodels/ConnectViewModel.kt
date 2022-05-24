package org.eson.liteble.viewmodels

import android.bluetooth.BluetoothGatt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shon.ble.data.ConnectResult
import com.shon.ble.util.BleLog
import com.shon.extble.suspendConnectAndDiscoverService
import kotlinx.coroutines.launch
import org.eson.liteble.data.AppCommonData


class ConnectViewModel : ViewModel() {

    val connectedState: MutableState<String> = mutableStateOf("DisConnect")
    val connectedGatt: MutableState<BluetoothGatt?> = mutableStateOf(null)
    val showLogWindow: MutableState<Boolean> = mutableStateOf(false)
    val showSendDataDialog:MutableState<Boolean> = mutableStateOf(false)
    fun startConnectDevice() {

        BleLog.d("startConnectDevice ===============")
        AppCommonData.selectDevice.value?.let {

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




}




