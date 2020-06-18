package org.e.ble.scan;

import android.Manifest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.RequiresPermission;

import org.e.ble.scan.callback.ScanCallback;
import org.e.ble.scan.support.ScanFilter;

import java.util.List;

public abstract class BleScannerCompat {
    private static BleScannerCompat mInstance;
    protected final Handler mHandler;

    public static BleScannerCompat getScanner() {
        if (mInstance != null) {
            return mInstance;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mInstance = new BleScannerImplL();
        }
        return mInstance = new BleScannerImplJB();
    }

    BleScannerCompat() {
        mHandler = new Handler(Looper.getMainLooper());
    }


    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void startScan(final ScanCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        startScanInternal(null, new ScanCfg.Builder().build(), callback);
    }


    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void startScan(final List<ScanFilter> filters, final ScanCfg settings, final ScanCallback callback) {
        if (settings == null || callback == null) {
            throw new IllegalArgumentException("settings or callback is null");
        }
        startScanInternal(filters, settings, callback);
    }


    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    protected abstract void startScanInternal(final List<ScanFilter> filters, final ScanCfg settings, final ScanCallback callback);

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public abstract void stopScan(final ScanCallback callback);

    public abstract void flushPendingScanResults(final ScanCallback callback);


}
