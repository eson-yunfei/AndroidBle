package org.eson.liteble.main

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.lifecycle.*
import no.nordicsemi.android.support.v18.scanner.*
import org.eson.liteble.common.share.ConfigPreferences
import java.util.*

/**
 * 扫描设备
 */


class ScannerViewModel
constructor(private val savedStateHandle: SavedStateHandle) : ViewModel(), LifecycleObserver {

    private val scannerResultLiveData = ScannerLiveData()

    private  var scannerCompat: BluetoothLeScannerCompat? = null
    private lateinit var scannerView: ScannerView

    fun attachView(lifecycleOwner: LifecycleOwner, scannerView: ScannerView) {
        this.scannerView = scannerView
        lifecycleOwner.lifecycle.addObserver(this)
        scannerResultLiveData.observe(lifecycleOwner) {
            it
            scannerView.onFindDevices(it.getScanResultList())
        }
    }

    fun startScanner() {
        Handler(Looper.getMainLooper()).postDelayed(ConfigPreferences.scannerTime.toLong()) {
            // 10 s  后 停止扫描
            stopScanner()
        }

        scannerResultLiveData.clearData()
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
        scannerCompat?.startScan(filters, scanCfg, scanCallback)

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onLifeOwnerPause() {
        scannerCompat?.stopScan(scanCallback)
    }

    fun stopScanner() {
        scannerCompat?.stopScan(scanCallback)
        scannerView.onFinishScanner()
    }


    private val scanCallback: ScanCallback = object : ScanCallback() {

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            scannerResultLiveData.onScannerResult(results)
        }

    }

    interface ScannerView {
        fun onFindDevices(scanResultList: MutableList<ScanResult>)

        fun onFinishScanner()
    }
}

