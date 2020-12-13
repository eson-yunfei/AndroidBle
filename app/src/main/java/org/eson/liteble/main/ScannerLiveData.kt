package org.eson.liteble.main

import android.text.TextUtils
import androidx.lifecycle.LiveData
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.common.share.ConfigPreferences
import org.eson.log.LogUtils

/**
 * 扫描结果的 livedata
 *
 * 包括对 扫描结果的一些处理和过滤
 */
class ScannerLiveData : LiveData<ScannerLiveData>() {

    private val scanResultList = mutableListOf<ScanResult>()
    fun getScanResultList(): MutableList<ScanResult> {
        //按照 信号值排序
        scanResultList.sortByDescending {
            it.rssi
        }
        return scanResultList
    }


    /**
     * 清空数据，一般在开始扫描之前调用
     */
    fun clearData() {
        scanResultList.clear()
    }


    /**
     * 添加扫描结果，进行处理
     */
    fun onScannerResult(results: List<ScanResult>) {
        results.filter {
            //是否 过滤名称为 null 的设备，
            //返回ture, 添加到列表中，false 则 丢弃此数据
            LogUtils.d("results = $it")
            if (ConfigPreferences.filterNoName) {
                !TextUtils.isEmpty(it.device.name)
            } else {
                true
            }
        }.forEach { result ->
            val index = scanResultList.indexOfFirst {
                //判断扫描到的设备是否已经在列表中存在
                TextUtils.equals(it.device.address, result.device.address)
            }
            if (index == -1) {
                //不存在，添加
                scanResultList.add(result)
            } else {
                //存在则替换数据
                scanResultList[index] = result
            }
        }

        //刷新数据
        postValue(this)

    }


}