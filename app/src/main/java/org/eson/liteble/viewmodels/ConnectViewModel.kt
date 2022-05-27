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
import com.shon.ble.util.ByteUtil
import com.shon.extble.suspendConnectAndDiscoverService
import com.shon.extble.suspendEnableNotification
import com.shon.extble.suspendReadInfo
import com.shon.extble.suspendWriteWithoutCallback
import kotlinx.coroutines.launch
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.logger.LogMessageBean
import java.util.*


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


    suspend fun enableNotify(
        gatt: BluetoothGatt,
        serviceUUID: String,
        characterUUID: String
    ): Boolean {
        BleLog.d("enableNotify ==========>>>>")
        BleLog.d("serviceUUID = $serviceUUID")
        BleLog.d("characterUUID = $characterUUID")
        return when (AppCommonData.selectDevice.value) {
            null -> false
            else -> {

                val address = gatt.device.address
                val enableNotificationResult =
                    suspendEnableNotification(
                        address,
                        gatt,
                        UUID.fromString(serviceUUID),
                        UUID.fromString(characterUUID)
                    )
                val content = "UUID: $characterUUID"
                val logMessageBean =
                    LogMessageBean(address, "Enable Notification ($address)", content)
                AppCommonData.addMessageList(logMessageBean)
                BleLog.d("enableNotification = $enableNotificationResult")
                enableNotificationResult
            }
        }
    }

    suspend fun readInfo(gatt: BluetoothGatt, serviceUUID: String, characterUUID: String): String? {
        val address = gatt.device.address
        AppCommonData.selectDevice.value ?: return null
        AppCommonData.selectDevice.value?.let {
            val readInfo = suspendReadInfo(
                it.device.address,
                gatt, serviceUUID, characterUUID
            ) {

                val content = "UUID: $characterUUID"
                val logMessageBean = LogMessageBean(address, "Read Info ($address)", content)
                AppCommonData.addMessageList(logMessageBean)
            }
            readInfo?.let { values ->
                val result =
                    ByteUtil.getHexString(values) + " (" + ByteUtil.byteToCharSequence(values) + ")"
                val logMessageBean = LogMessageBean(address, "Read Result ($address)", result)
                AppCommonData.addMessageList(logMessageBean)
                return result
            }

        }
        return null
    }


    fun writeData(
        gatt: BluetoothGatt,
        serviceUUID: String,
        characterUUID: String,
        sendData: String
    ) {
        val address = gatt.device.address
        viewModelScope.launch {
            val writeResult =
                suspendWriteWithoutCallback(address, gatt, serviceUUID, characterUUID, sendData) {
                    val content = "UUID: $characterUUID \nstart send: $sendData"
                    val logMessageBean = LogMessageBean(address, "Send Data ($address)", content)
                    AppCommonData.addMessageList(logMessageBean)
                }

            val content = "data: $sendData"
            val logMessageBean =
                LogMessageBean(address, "Send Data Result : $writeResult", content)
            AppCommonData.addMessageList(logMessageBean)
        }
    }

}




