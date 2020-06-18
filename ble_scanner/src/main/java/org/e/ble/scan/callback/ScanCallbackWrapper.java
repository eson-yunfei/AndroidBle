package org.e.ble.scan.callback;

import android.os.Handler;

import org.e.ble.scan.ScanCfg;
import org.e.ble.scan.support.ScanFilter;
import org.e.ble.scan.support.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * package_name  : org.e.ble.scan.callback
 * file_name     : ScanCallbackWrapper
 * create by     : xiaoyunfei
 * create date   : 2018/5/11
 * description   :
 */
public class ScanCallbackWrapper {

    private final Handler mHandler;
    private final List<ScanFilter> mFilters;
    private final ScanCfg mScanCfg;
    private final ScanCallback mScanCallback;
    private final List<ScanResult> mScanResults;
    private final List<String> mDevicesInBatch;


    private final Runnable mFlushPendingScanResultsTask = new Runnable() {
        @Override
        public void run() {
            flushPendingScanResults();
            mHandler.postDelayed(this, mScanCfg.getReportDelayMillis());
        }
    };

    public ScanCallbackWrapper(Handler handler,final List<ScanFilter> filters, final ScanCfg settings, final ScanCallback callback) {
        mHandler = handler;
        mFilters = filters;
        mScanCfg = settings;
        mScanCallback = callback;


        // Emulate batching
        final long delay = settings.getReportDelayMillis();
        if (delay > 0) {
            mScanResults = new ArrayList<>();
            mDevicesInBatch = new ArrayList<>();
            mHandler.postDelayed(mFlushPendingScanResultsTask, delay);
        } else {
            mScanResults = null;
            mDevicesInBatch = null;
        }
    }

    public void close() {
        if (mScanResults != null) {
            mHandler.removeCallbacks(mFlushPendingScanResultsTask);
        }
    }

    public ScanCfg getScanSettings() {
        return mScanCfg;
    }

    List<ScanFilter> getScanFilters() {
        return mFilters;
    }

    ScanCallback getScanCallback() {
        return mScanCallback;
    }

    public void flushPendingScanResults() {
        if (mScanResults != null) {
            synchronized (mScanResults) {
                mScanCallback.onBatchScanResults(mScanResults);
                mScanResults.clear();
                mDevicesInBatch.clear();
            }
        }
    }


    public void handleScanResult(final ScanResult scanResult) {
        if (mFilters != null && !mFilters.isEmpty() && !matches(scanResult))
            return;

        final String deviceAddress = scanResult.getDevice().getAddress();

        if (mScanCfg.getReportDelayMillis() > 0) {
            synchronized (mScanResults) {
                if (!mDevicesInBatch.contains(deviceAddress)) {
                    mScanResults.add(scanResult);
                    mDevicesInBatch.add(deviceAddress);
                }
            }
            return;
        }
        onScanResult(scanResult);
    }

    public void handleScanResults(final List<ScanResult> results, final boolean offloadedFilteringSupported) {
        List<ScanResult> filteredResults = results;

        if (mFilters != null && !offloadedFilteringSupported) {
            filteredResults = new ArrayList<>();
            for (final ScanResult result : results)
                if (matches(result))
                    filteredResults.add(result);
        }

        onBatchScanResults(filteredResults);
    }

    private boolean matches(final ScanResult result) {
        for (final ScanFilter filter : mFilters) {
            if (filter.matches(result))
                return true;
        }
        return false;
    }

    private void onScanResult(final ScanResult scanResult) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mScanCallback.onScanResult(scanResult);
            }
        });
    }

    private void onBatchScanResults(final List<ScanResult> results) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mScanCallback.onBatchScanResults(results);
            }
        });
    }


    public void onScanManagerErrorCallback(final int errorCode) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mScanCallback.onScanFailed(errorCode);
            }
        });

    }

}
