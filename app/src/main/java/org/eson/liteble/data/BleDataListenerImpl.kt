package org.eson.liteble.data

import com.shon.ble.listener.BleDataListener
import com.shon.ble.util.ByteUtil
import org.eson.liteble.logger.LogMessageBean

class BleDataListenerImpl private constructor(): BleDataListener {
    companion object{
        val dataListener by lazy { BleDataListenerImpl() }
    }

    override fun onReceiverData(address: String, value: ByteArray) {

        val content = ByteUtil.getHexString(value)
        val logMessageBean = LogMessageBean(address, "Receiver Data ($address)", content)
        AppCommonData.addMessageList(logMessageBean)

    }

}