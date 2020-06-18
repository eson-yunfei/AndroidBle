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
import android.os.Looper;
import android.os.ParcelUuid;
import android.text.TextUtils;

import com.e.ble.bean.BLEDevice;
import com.e.ble.support.BluetoothUuid;
import com.e.ble.support.ScanRecord;
import com.e.ble.util.BLELog;

import java.util.List;

/**
 * @package_name com.e.ble.scan
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/11/15.
 * @description
 */

class BLEScanCallback implements BluetoothAdapter.LeScanCallback {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private String[] nameFilters;
    private ParcelUuid[] uuidFilters;

    private BLEScanListener mBLEScanListener;

    public BLEScanCallback(BLEScanListener bleScanListener) {

        this.mBLEScanListener = bleScanListener;
    }

    public BLEScanCallback(String[] nameFilters, ParcelUuid[] uuidFilter, BLEScanListener bleScanListener) {

        this.nameFilters = nameFilters;
        this.uuidFilters = uuidFilter;

        this.mBLEScanListener = bleScanListener;
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

        if (nameFilters != null && nameFilters.length != 0) {
            if (!findName(device)) {
                return;
            }
        }

        ScanRecord sr = ScanRecord.parseFromBytes(scanRecord);
        if (uuidFilters != null && uuidFilters.length != 0) {
            if (!findUUID(sr)) {
                return;
            }
        }

        final BLEDevice bleDevice = new BLEDevice(device.getName(), device.getAddress(), rssi, sr);

        if (mBLEScanListener == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mBLEScanListener.onScanning(bleDevice);
            }
        });
    }

    /**
     * 名称过滤,区分大小写
     *
     * @param device
     * @return
     */
    private boolean findName(BluetoothDevice device) {

        if (nameFilters == null || nameFilters.length == 0) {
            return true;
        }

        String name = device.getName();
        if (TextUtils.isEmpty(name)) {
            return false;
        }

        boolean findName = false;

        for (int i = 0; i < nameFilters.length; i++) {
            String filter = nameFilters[i];
            if (filter.isEmpty()) {
                continue;
            }
            if (TextUtils.equals(name, filter) || name.contains(filter)) {
                findName = true;
                break;
            }
        }

        return findName;
    }

    /**
     * 过滤UUIDs
     *
     * @param sr
     * @return
     */
    private boolean findUUID(ScanRecord sr) {
        if (sr == null || uuidFilters == null || uuidFilters.length == 0) {
            return true;
        }
        //获取广播出来的UUID,不只一个
        List<ParcelUuid> parcelUuids = sr.getServiceUuids();

        if (parcelUuids == null || parcelUuids.size() == 0) {
            return false;
        }
        ParcelUuid[] parcelUuid = new ParcelUuid[parcelUuids.size()];
        for (int i = 0; i < parcelUuids.size(); i++) {
            parcelUuid[i] = parcelUuids.get(i);
        }

        boolean findUUID = BluetoothUuid.containsAnyUuid(parcelUuid, uuidFilters);
        BLELog.e("findUUID == " + findUUID);
        return findUUID;
    }
}