package org.eson.liteble.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shon.bluetooth.util.BleLog
import no.nordicsemi.android.support.v18.scanner.*

class ScanViewModel : ViewModel() {
    private var scannerCompat: BluetoothLeScannerCompat? = null
     val scanResult:MutableLiveData<MutableList<ScanResult>> = MutableLiveData(mutableListOf())
    fun startScanner() {
        scannerCompat?: kotlin.run {
            BleLog.d("startScanner")
            scannerCompat = BluetoothLeScannerCompat.getScanner()
            scannerCompat?.startScan(getScanFilter(), getScanSetting(), scanCallback)
        }
    }

    fun stopScanner() {
        scannerCompat?.stopScan(scanCallback)
    }

    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onBatchScanResults(results: MutableList<ScanResult>) {

            BleLog.d("results size  = ${results.size}")
            var list = scanResult.value
            list?: kotlin.run { list = mutableListOf() }
            list?.let {newList->
                results.forEach {result->
                    BleLog.d("result = ${result.device.address}")
                    val find = newList.find { result.device.address == it.device.address }
                    find?.let {
                        val index = newList.indexOf(it)
                        if (index != -1){
                            newList[index] = result
                        }
                    }
                    find?: kotlin.run { newList.add(result) }
                }
                scanResult.postValue(newList)
            }

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
            .setReportDelay(1_000)
            .build()
    }
}