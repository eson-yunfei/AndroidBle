package org.eson.liteble.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.logger.LogMessageBean

object AppCommonData {
    val filterNoName: MutableState<Boolean> = mutableStateOf(true)
    val sortByRssi: MutableState<Boolean> = mutableStateOf(true)

    val showSendDataDialog : MutableState<Boolean> = mutableStateOf(false)
    val sendDataCharacteristic:MutableState<SendCharacteristicsBean?> =mutableStateOf(null)

    val selectDevice: MutableState<ScanResult?> = mutableStateOf(null)

    val messageList:MutableList<LogMessageBean> = mutableStateListOf()

    fun addMessageList(messageBean: LogMessageBean){
        messageList.add(messageBean)
    }

    fun clearMessageList(){
        messageList.clear()
    }
}