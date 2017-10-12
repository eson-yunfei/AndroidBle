/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.e.ble.scan;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.ParcelUuid;

import com.e.ble.bean.BLEDevice;
import com.e.ble.check.BLECheck;
import com.e.ble.scan.appcompat.BLEScannerCompat;
import com.e.ble.scan.appcompat.ScanCallback;
import com.e.ble.scan.appcompat.ScanFilter;
import com.e.ble.scan.appcompat.ScanRecord;
import com.e.ble.scan.appcompat.ScanResult;
import com.e.ble.scan.appcompat.ScanSettings;
import com.e.ble.util.BLEError;
import com.e.ble.util.BLELog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 蓝牙扫描工具类
 * <p>
 * |只提供 开始扫描&停止扫描
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class BLEScanner implements BLEScanListener {
    //instance
    private static volatile BLEScanner bleScanner = null;

    public static final int INFINITE = -1;// 无限时长扫描，用户手动调用停止扫描
    public static final int DEFAULT = 0;//默认时长

    private int scanTime = 10000;            //扫描时长
    private String[] nameFilter = null;        //名称过滤
    private UUID[] uuidFilter = null;       //UUID 过滤

    private BLEScanListener bleScanListener;    //扫描监听
    private BLEDevice bleDevice = null;
    private BLEScannerCompat mScannerCompat;

    private Handler handler = null;

    private List<ScanFilter> filterList = new ArrayList<>();

    private BLEScanner() {
        mScannerCompat = BLEScannerCompat.getScanner();
    }

    /**
     * <p>初始化</p>
     */
    public static void init() {
        if (bleScanner == null) {
            synchronized (BLEScanner.class) {
                if (bleScanner == null) {
                    bleScanner = new BLEScanner();
                    BLELog.i("BLEScannerCompat init ok");
                }
            }
        }
    }

    public static BLEScanner get() {
        if (bleScanner == null) {
            init();
        }
        return bleScanner;
    }


    /**
     * 开始扫描设备<br/>
     *
     * @param bleScanCfg
     * @param scanListener
     */
    public void startScanner(BLEScanCfg bleScanCfg, BLEScanListener scanListener) {

        if (scanListener == null) {
            BLELog.e("scanListener is null");
            return;
        }
        this.bleScanListener = scanListener;

        if (!BLECheck.get().isBleEnable()) {
            //BLE not enable
            bleScanListener.onScannerError(BLEError.BLE_CLOSE);
            return;
        }

        initScanConfig(bleScanCfg);
        tryToStopScanner();
        startScanner();
    }

    public void stopScan() {
        tryToStopScanner();
    }

    private void initScanConfig(BLEScanCfg bleScanCfg) {
        if (bleScanCfg == null) {
            bleScanCfg = getDftCfg();
        }
        filterList.clear();
        scanTime = bleScanCfg.getScanTime();
        nameFilter = bleScanCfg.getNameFilter();
        uuidFilter = bleScanCfg.getUuidFilter();

        ScanFilter filter = null;
        if (nameFilter != null && nameFilter.length != 0) {
            for (int i = 0; i < nameFilter.length; i++) {
                filter = new ScanFilter.Builder()
                        .setDeviceName(nameFilter[i])
                        .build();
                filterList.add(filter);
            }
        }

        if (uuidFilter != null && uuidFilter.length != 0) {
            for (int i = 0; i < uuidFilter.length; i++) {
                filter = new ScanFilter.Builder()
                        .setServiceUuid(new ParcelUuid(uuidFilter[i]))
                        .build();
                filterList.add(filter);
            }
        }
    }

    /**
     * 尝试停止扫描设备
     */
    private void tryToStopScanner() {
        if (handler != null) {
            handler.removeCallbacks(stopScanRunnable);
            handler = null;
        }

        if (BLECheck.get().isBleEnable()) {
            mScannerCompat.stopScan(mScanCallback);
        }
    }

    /**
     * 开始扫描
     */
    private void startScanner() {

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();

        if (filterList.size() == 0) {
            mScannerCompat.startScan(null, scanSettings, mScanCallback);
        } else {
            mScannerCompat.startScan(filterList, scanSettings, mScanCallback);
        }
        if (scanTime == INFINITE) {
            return;
        }
        if (scanTime == DEFAULT) {
            scanTime = 10000;
        }
        startTimer();
    }

    /**
     * 开始扫描时长倒计时
     * <p>
     * 开始之前先尝试停止上一次的倒计时
     */
    private void startTimer() {
        handler = new Handler();
        handler.postDelayed(stopScanRunnable, scanTime);
    }


    /**
     * 定时需要执行的任务
     * <p>
     * 停止设备扫描
     */
    private Runnable stopScanRunnable = new Runnable() {
        @Override
        public void run() {
            tryToStopScanner();
            //回调扫描结束状态
            onScannerStop();
        }
    };

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            ScanRecord scanRecord = result.getScanRecord();
            getBleDevice(device, result.getRssi(), scanRecord);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            tryToStopScanner();
            onScannerError(errorCode);
        }
    };

    /**
     * 根据 BluetoothDevice  获取 BLEDevice
     *
     * @param device
     * @param rssi
     * @param scanRecord @return
     */
    private void getBleDevice(BluetoothDevice device, int rssi, ScanRecord scanRecord) {
        if (device == null) {
            return;
        }
        String name = BLEScanUtils.getDeviceName(device.getName(), scanRecord);

        //判断是否在名称过滤范围之内
        //如果不包含，不添加设备
        if (!BLEScanUtils.isFilterContainName(name, nameFilter)) {
            return;
        }
        // BLEDevice
        bleDevice = new BLEDevice();
        bleDevice.setName(name);
        bleDevice.setMac(device.getAddress());
        bleDevice.setRssi(rssi);

        bleDevice.setScanRecord(scanRecord);
        onScanning(bleDevice);
    }

    @Override
    public void onScannerStart() {
        if (bleScanListener != null) {
            bleScanListener.onScannerStart();
        }
    }

    @Override
    public void onScanning(BLEDevice device) {
        if (bleScanListener != null) {
            bleScanListener.onScanning(device);
        }
    }

    @Override
    public void onScannerStop() {
        if (bleScanListener != null) {
            bleScanListener.onScannerStop();
        }
    }

    @Override
    public void onScannerError(int errorCode) {
        if (bleScanListener != null) {
            bleScanListener.onScannerError(errorCode);
        }
    }

    private BLEScanCfg getDftCfg() {
        BLEScanCfg bleScanCfg = new BLEScanCfg.ScanCfgBuilder(DEFAULT).builder();
        return bleScanCfg;
    }
}