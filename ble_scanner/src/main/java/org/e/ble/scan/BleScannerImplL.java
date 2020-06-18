package org.e.ble.scan;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import org.e.ble.scan.callback.ScanCallback;
import org.e.ble.scan.callback.ScanCallbackWrapper;
import org.e.ble.scan.support.BluetoothLeUtils;
import org.e.ble.scan.support.ScanFilter;
import org.e.ble.scan.support.ScanRecord;
import org.e.ble.scan.support.ScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class BleScannerImplL extends BleScannerCompat {
    private final BluetoothAdapter mBluetoothAdapter;
    private final Map<ScanCallback, ScanCallbackWrapper> mWrappers;
    private final Map<ScanCallback, android.bluetooth.le.ScanCallback> mCallbacks;
    private final Map<android.bluetooth.le.ScanCallback, ScanCallbackWrapper> mWrappers2;
    private boolean offloadedFilteringSupported;

    public BleScannerImplL() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mWrappers = new HashMap<>();
        mWrappers2 = new HashMap<>();
        mCallbacks = new HashMap<>();
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    protected void startScanInternal(final List<ScanFilter> filters, final ScanCfg settings, final ScanCallback callback) {
        BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
        offloadedFilteringSupported = mBluetoothAdapter.isOffloadedFilteringSupported();

        if (mWrappers.containsKey(callback)) {
            throw new IllegalArgumentException("scanner already started with given callback");
        }

        final BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (scanner == null)
            throw new IllegalStateException("BT le scanner not available");

        final ScanCallbackWrapper wrapper = new ScanCallbackWrapper(mHandler,filters, settings, callback);
        final ScanCallbackImpl _callback = new ScanCallbackImpl();
        final android.bluetooth.le.ScanSettings _settings = toImpl(mBluetoothAdapter, settings);
        List<android.bluetooth.le.ScanFilter> _filters = null;
        if (filters != null && mBluetoothAdapter.isOffloadedFilteringSupported())
            _filters = toImpl(filters);

        mWrappers.put(callback, wrapper);
        mCallbacks.put(callback, _callback);
        mWrappers2.put(_callback, wrapper);

        scanner.startScan(_filters, _settings, _callback);
    }

    @Override
    public void stopScan(final ScanCallback callback) {
        final ScanCallbackWrapper wrapper = mWrappers.get(callback);
        if (wrapper == null)
            return;

        wrapper.close();
        mWrappers.remove(callback);
        android.bluetooth.le.ScanCallback _callback = mCallbacks.get(callback);
        mCallbacks.remove(callback);
        mWrappers2.remove(_callback);

        final BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (scanner == null)
            return;

        scanner.stopScan(_callback);
    }

    @Override
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void flushPendingScanResults(final ScanCallback callback) {
        BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null!");
        }

        final ScanCallbackWrapper wrapper = mWrappers.get(callback);
        if (wrapper == null) {
            throw new IllegalArgumentException("callback not registered!");
        }

        if (mBluetoothAdapter.isOffloadedScanBatchingSupported())
            mBluetoothAdapter.getBluetoothLeScanner().flushPendingScanResults(mCallbacks.get(callback));
        else
            mWrappers.get(callback).flushPendingScanResults();
    }

    private class ScanCallbackImpl extends android.bluetooth.le.ScanCallback {
        private long mLastBatchTimestamp;

        @Override
        public void onScanResult(final int callbackType, final android.bluetooth.le.ScanResult _result) {
            final ScanCallbackWrapper wrapper = mWrappers2.get(this);
            if (wrapper != null) {
                final byte[] data = _result.getScanRecord() != null ? _result.getScanRecord().getBytes() : null;
                final ScanResult result = new ScanResult(_result.getDevice(), ScanRecord.parseFromBytes(data), _result.getRssi(), _result.getTimestampNanos());

                wrapper.handleScanResult(result);
            }
        }

        @Override
        public void onBatchScanResults(final List<android.bluetooth.le.ScanResult> _results) {
            final ScanCallbackWrapper wrapper = mWrappers2.get(this);
            if (wrapper != null) {
                final long now = SystemClock.elapsedRealtime();
                if (mLastBatchTimestamp > now - wrapper.getScanSettings().getReportDelayMillis() + 5) {
                    return;
                }
                mLastBatchTimestamp = now;

                final List<ScanResult> results = new ArrayList<>();
                for (final android.bluetooth.le.ScanResult _result : _results) {
                    final byte[] data = _result.getScanRecord() != null ? _result.getScanRecord().getBytes() : null;
                    final ScanResult result = new ScanResult(_result.getDevice(), ScanRecord.parseFromBytes(data), _result.getRssi(), _result.getTimestampNanos());
                    results.add(result);
                }

                wrapper.handleScanResults(results, offloadedFilteringSupported);
            }
        }

        @Override
        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
        public void onScanFailed(final int errorCode) {
            final ScanCallbackWrapper wrapper = mWrappers2.get(this);
            if (wrapper == null)
                return;

            wrapper.onScanManagerErrorCallback(errorCode);
        }
    }

    android.bluetooth.le.ScanSettings toImpl(@NonNull final BluetoothAdapter adapter, @NonNull final ScanCfg settings) {
        final android.bluetooth.le.ScanSettings.Builder builder = new android.bluetooth.le.ScanSettings.Builder();

        if (adapter.isOffloadedScanBatchingSupported())
            builder.setReportDelay(settings.getReportDelayMillis());


        return builder.build();
    }

    List<android.bluetooth.le.ScanFilter> toImpl(final @NonNull List<ScanFilter> filters) {
        final List<android.bluetooth.le.ScanFilter> _filters = new ArrayList<>();
        for (final ScanFilter filter : filters)
            _filters.add(toImpl(filter));
        return _filters;
    }

    android.bluetooth.le.ScanFilter toImpl(final ScanFilter filter) {
        final android.bluetooth.le.ScanFilter.Builder builder = new android.bluetooth.le.ScanFilter.Builder();
        builder.setDeviceAddress(filter.getDeviceAddress())
                .setDeviceName(filter.getDeviceName())
                .setServiceUuid(filter.getServiceUuid(), filter.getServiceUuidMask())
                .setManufacturerData(filter.getManufacturerId(), filter.getManufacturerData(), filter.getManufacturerDataMask());

        if (filter.getServiceDataUuid() != null)
            builder.setServiceData(filter.getServiceDataUuid(), filter.getServiceData(), filter.getServiceDataMask());

        return builder.build();
    }
}
