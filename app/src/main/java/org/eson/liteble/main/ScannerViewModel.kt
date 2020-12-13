package org.eson.liteble.main

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import no.nordicsemi.android.support.v18.scanner.*
import org.eson.liteble.common.share.ConfigPreferences

/**
 * 扫描设备
 */
class ScannerViewModel @ViewModelInject
constructor(@Assisted private val savedStateHandle: SavedStateHandle) : ViewModel(), LifecycleObserver {

    private val scannerResultLiveData = ScannerLiveData()

    private  var scannerCompat: BluetoothLeScannerCompat? = null
    private lateinit var scannerView: ScannerView

    private var handler:Handler? = null
    fun attachView(lifecycleOwner: LifecycleOwner, scannerView: ScannerView) {
        this.scannerView = scannerView
        lifecycleOwner.lifecycle.addObserver(this)
        //监听 ScannerLiveData
        scannerResultLiveData.observe(lifecycleOwner, Observer {
            scannerView.onFindDevices(it.getScanResultList())
        })
    }

    fun startScanner() {
        if (handler == null){
            handler = Handler(Looper.getMainLooper())
        }
        handler!!.postDelayed(ConfigPreferences.scannerTime.toLong()) {
            //  根据设置的扫描时长，然后 停止扫描
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
       stopScanner()
    }

    fun stopScanner() {
        if (handler != null){
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
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

