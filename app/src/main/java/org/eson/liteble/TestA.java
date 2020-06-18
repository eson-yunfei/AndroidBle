package org.eson.liteble;

import android.app.Activity;
import android.os.Bundle;
import android.os.ParcelUuid;
import androidx.annotation.Nullable;
import android.util.Log;

import org.e.ble.scan.BleScannerCompat;
import org.e.ble.scan.callback.ScanCallback;
import org.e.ble.scan.support.ScanFilter;
import org.e.ble.scan.support.ScanResult;
import org.e.ble.scan.ScanCfg;

import java.util.ArrayList;
import java.util.List;

/**
 * package_name  : org.eson.liteble
 * file_name     : TestA
 * create by     : xiaoyunfei
 * create date   : 2018/5/10
 * description   :
 */
public class TestA extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BleScannerCompat scannerCompat = BleScannerCompat.getScanner();
        ScanCfg scanCfg = new ScanCfg.Builder()
//                .setReportDelay(20)
                .build();

        List<ScanFilter> filters = new ArrayList<>();
        ScanFilter filter = new ScanFilter.Builder()
                .setServiceUuid(ParcelUuid.fromString("6E401892-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setDeviceAddress("F7:CE:C8:F0:77:44")
                .build();

//        filters.add(filter);
        scannerCompat.startScan(null, scanCfg,scanCallback);


    }

    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(ScanResult result) {
            super.onScanResult(result);
            Log.e("onScanResult",result.toString());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < results.size(); i++) {
                builder.append(results.get(i).toString());
                builder.append("\n");

            }
            Log.e("onBatchScanResults",builder.toString());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };


}
