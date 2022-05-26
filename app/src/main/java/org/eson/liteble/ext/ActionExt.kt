package org.eson.liteble.ext

import android.bluetooth.BluetoothGatt
import com.shon.ble.call.WriteDataCall
import com.shon.ble.util.ByteUtil
import com.shon.extble.suspendEnableNotification
import com.shon.extble.suspendReadInfo
import org.eson.liteble.TestHistoricalData
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.logger.LogMessageBean
import java.util.*

object ActionExt {

    suspend fun enableNotify(gatt: BluetoothGatt, serviceUUID: UUID, characterUUID: UUID): Boolean {
        return when (AppCommonData.selectDevice.value) {
            null -> false
            else -> {
                val address = gatt.device.address
                val enableNotification =
                    suspendEnableNotification(address, gatt, serviceUUID, characterUUID)
                val content = "UUID: $characterUUID"
                val logMessageBean =
                    LogMessageBean(address, "Enable Notification ($address)", content)
                AppCommonData.addMessageList(logMessageBean)
                enableNotification
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


    fun testWritData(
        gatt: BluetoothGatt,
        serviceUUID: UUID,
        characterUUID: UUID
    ) {
        val address = gatt.device.address
        WriteDataCall(address, gatt, serviceUUID, characterUUID).enqueue(
            TestHistoricalData()
        )
    }

    fun writeData(
        gatt: BluetoothGatt,
        serviceUUID: UUID,
        characterUUID: UUID,
        sendData: String
    ) {
        val address = gatt.device.address
        WriteDataCall(address, gatt, serviceUUID, characterUUID).enqueue(
            object : WriteDataCallback(sendData) {
                override fun onSendResult(sendResult: Boolean) {
                    val content = "data: $sendData"
                    val logMessageBean =
                        LogMessageBean(address, "Send Data Result $sendResult)", content)
                    AppCommonData.addMessageList(logMessageBean)
                }

                override fun onExecuted() {
                    val content = "UUID: $characterUUID \nstart send: $sendData"
                    val logMessageBean = LogMessageBean(address, "Send Data ($address)", content)
                    AppCommonData.addMessageList(logMessageBean)
                }
            }
        )
    }
}