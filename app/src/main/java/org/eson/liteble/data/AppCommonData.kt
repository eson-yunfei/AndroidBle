package org.eson.liteble.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.logger.LogMessageBean

object AppCommonData {
    //隐藏无名称设备，默认隐藏
    val hideNoNameState: MutableState<Boolean> = mutableStateOf(true)

    //隐藏低 Rssi 设备
    val hideLowRssiState: MutableState<Boolean> = mutableStateOf(true)

    //低信号阀值
    val lowRssiThreshold: MutableState<Int> = mutableStateOf(-80)

//    //rssi 排序
    val sortByRssi: MutableState<Boolean> = mutableStateOf(true)

    //rssi 排序,默认升序
    val rssiSortType: MutableState<SortType> = mutableStateOf(SortType.ASC)

    val sendDataCharacteristic: MutableState<SendCharacteristicsBean?> = mutableStateOf(null)

    val selectDevice: MutableState<ScanResult?> = mutableStateOf(null)

    val messageList: MutableList<LogMessageBean> = mutableStateListOf()

    fun addMessageList(messageBean: LogMessageBean) {
        messageList.add(messageBean)
    }

    fun clearMessageList() {
        messageList.clear()
    }
}