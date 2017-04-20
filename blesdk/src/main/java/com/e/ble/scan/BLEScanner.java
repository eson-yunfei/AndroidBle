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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;

import com.e.ble.BLESdk;
import com.e.ble.bean.BLEDevice;
import com.e.ble.check.BLECheck;
import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLEError;
import com.e.ble.util.BLELog;
import com.e.ble.support.ScanRecord;

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

    public static final int INFINITE = -1;// 无限时长扫描，用户手动调用停止扫描
    public static final int DEFAULT = 0;//默认时长

    //instance
    private static volatile BLEScanner bleScanner = null;

    private BLEScanner() {

    }

    public static void init() {
        if (bleScanner == null) {
            synchronized (BLEScanner.class) {
                if (bleScanner == null) {
                    bleScanner = new BLEScanner();
                    BLELog.i("BLEScanner init ok");
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


    private int scanTime = 10000;            //扫描时长
    private String[] nameFilter = null;        //名称过滤
    private UUID[] uuidFilter = null;       //UUID 过滤
    private BLEScanListener bleScanListener;    //扫描监听


    /**
     * 开始扫描设备
     *
     * @param bleScanCfg
     * @param scanListener
     */
    public synchronized void startScanner(BLEScanCfg bleScanCfg, BLEScanListener scanListener) {

        if (scanListener == null) {
            bleScanListener.onScannerError(BLEError.BLE_SCANNER_CALLBACK_NULL);
            return;
        }

        this.bleScanListener = scanListener;
        if (!BLECheck.get().isBleEnable()) {
            //BLE not enable
            bleScanListener.onScannerError(BLEError.BLE_CLOSE);
            return;
        }

        if (bleScanCfg == null) {
            bleScanCfg = getDftCfg();
        }
        scanTime = bleScanCfg.getScanTime();
        nameFilter = bleScanCfg.getNameFilter();
        uuidFilter = bleScanCfg.getUuidFilter();


        tryToStopScanner();
        startScanner();

    }

    public synchronized void stopScan() {
        tryToStopScanner();
    }

    /**
     * 尝试停止扫描设备
     */
    private synchronized void tryToStopScanner() {
        if (handler != null) {
            handler.removeCallbacks(stopScanRunnable);
            handler = null;
        }

        if (BLECheck.get().isBleEnable()) {
            bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
            bluetoothAdapter.stopLeScan(scanCallback);
        }
    }

    private BluetoothAdapter bluetoothAdapter;

    /**
     * 开始扫描
     */
    private void startScanner() {
        bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
        bluetoothAdapter.startLeScan(uuidFilter, scanCallback);

        if (scanTime == INFINITE) {
            return;
        }
        if (scanTime == DEFAULT) {
            scanTime = 10000;
        }
        startTimer();
    }


    private Handler handler = null;

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


    /**
     * 系统的设备扫描回调监听
     * <p>
     * SDK 加入了重复地址过滤，
     * <p>
     * 用户不需要再次过滤
     */
    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if (bleScanListener == null) {
                return;
            }

            BLEDevice bleDevice = getBleDevice(device, rssi, scanRecord);
            if (bleDevice == null) {
                return;
            }

            //返回设备
            onScanning(bleDevice);
        }
    };

    /**
     * 根据 BluetoothDevice  获取 BLEDevice
     *
     * @param device
     * @param rssi
     * @param scanRecord @return
     */
    private BLEDevice getBleDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {

        String name = getDeviceName(device, scanRecord);
        //获取设备名称，名称有可能为： 空字符

        if (TextUtils.isEmpty(name)) {
            name = "< UnKnow >";
        }

        //判断是否在名称过滤范围之内
        //如果不包含，不添加设备
        if (!containName(name)) {
            return null;
        }

        // BLEDevice
        BLEDevice bleDevice = new BLEDevice();
        bleDevice.setName(name);
        bleDevice.setMac(device.getAddress());
        bleDevice.setRssi(rssi);
        if (scanRecord == null || scanRecord.length == 0) {
            return bleDevice;
        }
        ScanRecord record = ScanRecord.parseFromBytes(scanRecord);
        if (record == null) {
            return bleDevice;
        }
        BLELog.i(record.toString());
        bleDevice.setScanRecord(record);

        return bleDevice;
    }

    /**
     * 获取设备名称
     *
     * @param device
     * @param scanRecord
     * @return
     */
    private String getDeviceName(BluetoothDevice device, byte[] scanRecord) {

        String name = device.getName();
        if (!TextUtils.isEmpty(name)) {
            return name;
        }
        if (scanRecord == null || scanRecord.length == 0) {
            return name;
        }
        ScanRecord record = ScanRecord.parseFromBytes(scanRecord);

        if (record == null) {
            return name;
        }
        name = record.getDeviceName();
        return name;
    }

    /**
     * 检测设备名称是否在过滤名称范围内
     *
     * @param name
     * @return 如果包含设备名称，返回true
     */
    private boolean containName(String name) {

        if (nameFilter == null || nameFilter.length == 0) {
            return true;
        }
        boolean isContain = false;
        for (String aNameFilter : nameFilter) {

            if (name.equals(aNameFilter)) {
                isContain = true;
            }
            break;
        }

        return isContain;
    }


    @Override
    public void onScannerStart() {
        if (bleScanListener != null) {
            bleScanListener.onScannerStart();
        }
    }

    @Override
    public void onScanning(BLEDevice device) {
        bleScanListener.onScanning(device);
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
