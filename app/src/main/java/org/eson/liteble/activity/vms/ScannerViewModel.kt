package org.eson.liteble.activity.vms

import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import no.nordicsemi.android.support.v18.scanner.*
import org.eson.liteble.activity.vms.data.ScannerLiveData
import java.util.*

/**
 * 扫描设备
 */
class ScannerViewModel : ViewModel() {

    private val scannerResultLiveData = ScannerLiveData()

    private lateinit var scannerCompat: BluetoothLeScannerCompat

    fun attachView(lifecycleOwner: LifecycleOwner, scannerView: ScannerView) {
        scannerResultLiveData.observe(lifecycleOwner) {
            it ?: return@observe
            scannerView.onFindDevices(it.getScanResultList())
        }
    }

    fun startScanner() {
        Handler(Looper.getMainLooper()).postDelayed(10_000) {
            // 10 s  后 停止扫描
            stopScanner()
        }

        scannerCompat = BluetoothLeScannerCompat.getScanner()
        val scanCfg = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setLegacy(false)
                .setReportDelay(1000)
                .setUseHardwareBatchingIfSupported(false)
                .build()
        val filters: MutableList<ScanFilter> = mutableListOf()
//        val filter = ScanFilter.Builder()
//                .setServiceUuid(ParcelUuid.fromString("6E401892-B5A3-F393-E0A9-E50E24DCCA9E"))
//                .setDeviceAddress("F7:CE:C8:F0:77:44")
//                .build()
//
//        filters.add(filter)
        scannerCompat.startScan(filters, scanCfg, scanCallback)
    }

    fun stopScanner() {
        scannerCompat.stopScan(scanCallback)
    }


    private val scanCallback: ScanCallback = object : ScanCallback() {

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            scannerResultLiveData.onScannerResult(results)
        }

    }

    interface ScannerView {
        fun onFindDevices(scanResultList: MutableList<ScanResult>)
    }
}

