package org.eson.liteble.viewmodels

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shon.ble.util.BleLog
import no.nordicsemi.android.support.v18.scanner.*
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.data.SortType

class ScanViewModel : ViewModel() {
    private var scannerCompat: BluetoothLeScannerCompat? = null
    val scanResult: MutableLiveData<MutableList<ScanResult>> = MutableLiveData(mutableListOf())

    fun startScanner() {
        scannerCompat ?: kotlin.run {
            BleLog.d("startScanner")
            scannerCompat = BluetoothLeScannerCompat.getScanner()
            scannerCompat?.startScan(getScanFilter(), getScanSetting(), scanCallback)
        }
    }

    fun stopScanner() {
        BleLog.d("stopScanner")
        scannerCompat?.stopScan(scanCallback)
        scannerCompat = null
    }

    private val scanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            BleLog.d("results size  = ${results.size}")
            val hideNameList = getHideNameList(results)
            val hideLowRssiList = getHideLowRssiList(hideNameList)

            var list = scanResult.value
            list ?: kotlin.run { list = mutableListOf() }
            list?.let { newList ->
                val hideNameExistList = getHideNameList(newList)
                val hideLowRssiExistList = getHideLowRssiList(hideNameExistList)
                hideLowRssiList.forEach { result ->
                    val find = hideLowRssiExistList.find { result.device.address == it.device.address }
                    find?.let {
                        val index = hideLowRssiExistList.indexOf(it)
                        if (index != -1) {
                            hideLowRssiExistList[index] = result
                        }
                    }
                    find ?: kotlin.run { hideLowRssiExistList.add(result) }
                }
                val sortList = getSortList(hideLowRssiExistList)
                scanResult.postValue(sortList)
            }

        }
    }


    @SuppressLint("MissingPermission")
    private fun getHideNameList(results: MutableList<ScanResult>): MutableList<ScanResult> {

        return if (AppCommonData.hideNoNameState.value) {
            results.filter { !TextUtils.isEmpty(it.device.name) }.toMutableList()
        } else {
            results
        }
    }

    private fun getHideLowRssiList(results: MutableList<ScanResult>): MutableList<ScanResult> {
        return if (AppCommonData.hideLowRssiState.value) {
            results.filter { it.rssi > AppCommonData.lowRssiThreshold.value }.toMutableList()
        } else {
            results
        }
    }


    private fun getSortList(results: MutableList<ScanResult>): MutableList<ScanResult> {
        return when (AppCommonData.rssiSortType.value) {
            SortType.NONE -> results
            SortType.ASC -> results.sortedBy { it.rssi * -1 }.toMutableList()
            SortType.DESC -> results.sortedByDescending { it.rssi * -1 }.toMutableList()
        }
    }


    private fun getScanFilter(): MutableList<ScanFilter> {
        val scanFilterList: MutableList<ScanFilter> = mutableListOf()
        scanFilterList.add(
            ScanFilter.Builder()
                .build()
        )
        return scanFilterList
    }

    private fun getScanSetting(): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setLegacy(false)
            .setUseHardwareBatchingIfSupported(false)
            .setReportDelay(2_000)
            .build()
    }
}