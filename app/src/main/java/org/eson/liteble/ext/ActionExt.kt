package org.eson.liteble.ext

import android.bluetooth.BluetoothGatt
import com.shon.ble.util.ByteUtil
import com.shon.extble.suspendReadInfo
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.logger.LogMessageBean

object ActionExt {

    suspend fun readInfo(gatt: BluetoothGatt, serviceUUID: String, characterUUID: String): String? {
        val address = gatt.device.address
        AppCommonData.selectDevice.value ?: return null
        AppCommonData.selectDevice.value?.let {
            val readInfo = suspendReadInfo(
                it.device.address,
                gatt, serviceUUID, characterUUID
            ) {

                val content = "UUID: $characterUUID \n"
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


}