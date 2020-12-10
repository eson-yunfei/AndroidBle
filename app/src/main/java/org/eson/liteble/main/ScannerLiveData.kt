package org.eson.liteble.main

import android.text.TextUtils
import androidx.lifecycle.LiveData
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.common.share.ConfigPreferences
import org.eson.log.LogUtils

class ScannerLiveData : LiveData<ScannerLiveData>() {


    private val scanResultList = mutableListOf<ScanResult>()

    fun getScanResultList(): MutableList<ScanResult> {
        scanResultList.sortByDescending {
            it.rssi
        }
        return scanResultList
    }

    fun clearData(){
        scanResultList.clear()
    }

    fun onScannerResult(results: List<ScanResult>) {
        val nonNullNameList = results.filter {
            //过滤名称为 null 的设备
            LogUtils.d("results = $it")
            if (ConfigPreferences.filterNoName) {
                !TextUtils.isEmpty(it.device.name)
            } else {
                true
            }
        }

        if (nonNullNameList.isEmpty()) {
            return
        }

        if (scanResultList.isEmpty()) {
            //首次全部添加数据
            scanResultList.addAll(nonNullNameList)

            postValue(this)
            return
        }
        nonNullNameList.forEach { result ->
            val index = scanResultList.indexOfFirst {
                TextUtils.equals(it.device.address, result.device.address)
            }
            if (index == -1) {
                scanResultList.add(result)
            } else {
                scanResultList[index] = result
            }
        }
        postValue(this)
    }


}