package org.e.ble.scan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresPermission;

import org.e.ble.scan.callback.ScanCallback;
import org.e.ble.scan.callback.ScanCallbackWrapper;
import org.e.ble.scan.support.BluetoothLeUtils;
import org.e.ble.scan.support.ScanFilter;
import org.e.ble.scan.support.ScanRecord;
import org.e.ble.scan.support.ScanResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class BleScannerImplJB extends BleScannerCompat implements BluetoothAdapter.LeScanCallback {
    private final BluetoothAdapter mBluetoothAdapter;
    private final Map<ScanCallback, ScanCallbackWrapper> mWrappers;
    private long mPowerSaveRestInterval;
    private long mPowerSaveScanInterval;
    private Handler mPowerSaveHandler;
    private Runnable mPowerSaveSleepRunnable = new Runnable() {
        @SuppressWarnings("deprecation")
        @Override
        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
        public void run() {
            if (mBluetoothAdapter != null && mPowerSaveRestInterval > 0 && mPowerSaveScanInterval > 0) {
                mBluetoothAdapter.stopLeScan(BleScannerImplJB.this);

                if (mPowerSaveHandler != null) {
                    mPowerSaveHandler.postDelayed(mPowerSaveScanRunnable, mPowerSaveRestInterval);
                }
            }
        }
    };

    private Runnable mPowerSaveScanRunnable = new Runnable() {
        @SuppressWarnings("deprecation")
        @Override
        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
        public void run() {
            if (mBluetoothAdapter != null && mPowerSaveRestInterval > 0 && mPowerSaveScanInterval > 0) {
                mBluetoothAdapter.startLeScan(BleScannerImplJB.this);

                if (mPowerSaveHandler != null) {
                    mPowerSaveHandler.postDelayed(mPowerSaveSleepRunnable, mPowerSaveScanInterval);
                }
            }
        }
    };

    public BleScannerImplJB() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mWrappers = new HashMap<>();
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    @SuppressWarnings("deprecation")
    protected void startScanInternal(final List<ScanFilter> filters, final ScanCfg settings, final ScanCallback callback) {
        BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);

        if (mWrappers.containsKey(callback)) {
            throw new IllegalArgumentException("scanner already started with given callback");
        }

        boolean shouldStart;
        synchronized (mWrappers) {
            shouldStart = mWrappers.isEmpty();

            final ScanCallbackWrapper wrapper = new ScanCallbackWrapper(mHandler,filters, settings, callback);
            mWrappers.put(callback, wrapper);
        }

        setPowerSaveSettings();

        if (shouldStart) {
            mBluetoothAdapter.startLeScan(this);
        }
    }

    private void setPowerSaveSettings() {
        long minRest = Long.MAX_VALUE, minScan = Long.MAX_VALUE;
        synchronized (mWrappers) {
            for (ScanCallbackWrapper wrapper : mWrappers.values()) {
                final ScanCfg settings = wrapper.getScanSettings();
                if (settings.hasPowerSaveMode()) {
                    if (minRest > settings.getPowerSaveRest()) {
                        minRest = settings.getPowerSaveRest();
                    }
                    if (minScan > settings.getPowerSaveScan()) {
                        minScan = settings.getPowerSaveScan();
                    }
                }
            }
        }
        if (minRest < Long.MAX_VALUE && minScan < Long.MAX_VALUE) {
            mPowerSaveRestInterval = minRest;
            mPowerSaveScanInterval = minScan;
            if (mPowerSaveHandler == null) {
                mPowerSaveHandler = new Handler();
            } else {
                mPowerSaveHandler.removeCallbacks(mPowerSaveScanRunnable);
                mPowerSaveHandler.removeCallbacks(mPowerSaveSleepRunnable);
            }
            mPowerSaveHandler.postDelayed(mPowerSaveSleepRunnable, mPowerSaveScanInterval);
        } else {
            mPowerSaveRestInterval = mPowerSaveScanInterval = 0;
            if (mPowerSaveHandler != null) {
                mPowerSaveHandler.removeCallbacks(mPowerSaveScanRunnable);
                mPowerSaveHandler.removeCallbacks(mPowerSaveSleepRunnable);
            }
        }
    }

    @Override
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    @SuppressWarnings("deprecation")
    public void stopScan(final ScanCallback callback) {
        synchronized (mWrappers) {
            final ScanCallbackWrapper wrapper = mWrappers.get(callback);
            if (wrapper == null)
                return;

            mWrappers.remove(callback);
            wrapper.close();
        }

        setPowerSaveSettings();

        if (mWrappers.isEmpty()) {
            mBluetoothAdapter.stopLeScan(this);
        }
    }

    @Override
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void flushPendingScanResults(final ScanCallback callback) {
        BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null!");
        }
        mWrappers.get(callback).flushPendingScanResults();
    }

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        final ScanResult scanResult = new ScanResult(device, ScanRecord.parseFromBytes(scanRecord), rssi, SystemClock.elapsedRealtimeNanos());

        synchronized (mWrappers) {
            final Collection<ScanCallbackWrapper> wrappers = mWrappers.values();
            for (final ScanCallbackWrapper wrapper : wrappers) {
                wrapper.handleScanResult(scanResult);
            }
        }
    }
}
