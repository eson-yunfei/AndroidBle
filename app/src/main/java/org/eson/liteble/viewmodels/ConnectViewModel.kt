package org.eson.liteble.viewmodels

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shon.ble.BleManager
import com.shon.ble.data.ConnectResult
import com.shon.ble.gatt.BleConnectStateListener
import com.shon.ble.util.BleLog
import com.shon.extble.suspendConnectAndDiscoverService
import kotlinx.coroutines.launch
import org.eson.liteble.data.AppCommonData


class ConnectViewModel : ViewModel() {

    val connectedGatt: MutableState<BluetoothGatt?> = mutableStateOf(null)
    val showLogWindow: MutableState<Boolean> = mutableStateOf(false)

    val connectResultState: MutableState<ConnectResult> =
        mutableStateOf(ConnectResult.Disconnect(""))

    fun startConnectDevice() {

        BleLog.d("startConnectDevice ===============")
        AppCommonData.selectDevice.value?.let {
            val address = it.device.address
            connectResultState.value = ConnectResult.Connecting(address)
            viewModelScope.launch {

                val connectResult = suspendConnectAndDiscoverService(address)
                BleLog.d("connectResult = $connectResult")
                connectResultState.value = connectResult

                when (connectResult) {
                    is ConnectResult.ConnectSuccess -> {
                        connectedGatt.value = connectResult.gatt
                        startObserverConnectState()
                    }
                    else -> {
                        connectedGatt.value = null
                    }

                }
            }

        }
    }

    private fun startObserverConnectState() {
        BleManager.getManager().setConnectionStateListener(object : BleConnectStateListener {
            override fun onDeviceConnected(address: String, gatt: BluetoothGatt?) {
                BleLog.d("onDeviceConnected = $address")
//                connectResultState.value = ConnectResult.ConnectSuccess(address, gatt!!)
//                connectedGatt.value = gatt
            }

            @SuppressLint("MissingPermission")
            override fun onDeviceDisconnected(address: String) {
                BleLog.d("onDeviceDisconnected = $address")
                connectResultState.value = ConnectResult.Disconnect(address)
                connectedGatt.value?.close()
                connectedGatt.value = null

            }

        })
    }


    fun changeConnectState() {

        when (connectResultState.value) {
            is ConnectResult.ConnectSuccess -> disconnect()
            is ConnectResult.Connecting -> return
            else -> startConnectDevice()
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        connectedGatt.value?.disconnect()
    }

}




