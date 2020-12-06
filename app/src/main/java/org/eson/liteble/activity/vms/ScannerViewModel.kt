package org.eson.liteble.activity.vms;

import androidx.lifecycle.ViewModel;

import com.e.ble.bean.BLEDevice;
import com.e.ble.scan.BLEScanCfg;
import com.e.ble.scan.BLEScanListener;
import com.e.ble.scan.BLEScanner;

import org.eson.liteble.activity.vms.data.ScanLiveData;
import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 15:09
 * Package name : org.eson.liteble.activity.vms
 * Des :
 */
public class ScannerViewModel extends ViewModel {

    private ScanLiveData scanLiveData;

    public ScanLiveData startScanDevice(boolean isFilterName, int connectTime) {

        LogUtil.e("startScanDevice: isFilterName = " + isFilterName + " ; connectTime = " + connectTime);
        scanLiveData = new ScanLiveData(isFilterName);
        BLEScanCfg scanCfg = new BLEScanCfg.ScanCfgBuilder(connectTime)
                .builder();
        BLEScanner.get().startScanner(scanCfg, new BLEScanListener() {
            @Override
            public void onScanning(BLEDevice device) {
                LogUtil.d("onScanning : " + device.toString());
                scanLiveData.addScanBLE(device);
            }


            @Override
            public void onScannerStop() {
                LogUtil.e("onScannerStop : " );
                scanLiveData.setStop(true);
            }

            @Override
            public void onScannerError(int errorCode) {
                LogUtil.e("onScannerError : " + errorCode);
                scanLiveData.setTimeout(true);
            }

        });

        return scanLiveData;
    }

    public void stopScanner() {

    }
}
