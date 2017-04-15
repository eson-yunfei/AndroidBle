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

package com.e.ble.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.os.DeadObjectException;

import com.e.ble.BLESdk;
import com.e.ble.util.BLELog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @author xiaoyunfei
 * @date: 2017/3/19
 * @Description： BLEConnectList  连接的设备列表的管理类
 * <p>
 * 提供添加，删除，断开连接
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */

class BLEConnectList {
    private static BLEConnectList sBLEConnectList = null;

    private HashMap<String, BluetoothGatt> mGattHashMap;

    private BLEConnectList() {
        if (mGattHashMap == null) {
            mGattHashMap = new HashMap<>();
        }
    }

    public static BLEConnectList get() {
        if (sBLEConnectList == null) {
            sBLEConnectList = new BLEConnectList();
        }
        return sBLEConnectList;
    }

    /**
     * 是否超出设置的
     * 最大连接设备个数
     *
     * @param address
     * @return
     */
    public boolean outLimit(String address) {
        return !(mGattHashMap == null || mGattHashMap.size() == 0) && !mGattHashMap.containsKey(address) && mGattHashMap.size() >= BLESdk.get().getMaxConnect();

    }

    /**
     * 添加新的连接设备
     *
     * @param address
     * @param gatt
     */
    public void putGatt(String address, BluetoothGatt gatt) {
        mGattHashMap.put(address, gatt);
    }

    /**
     * 根据设备 mac 获取 BluetoothGatt
     *
     * @param address
     * @return
     */
    public BluetoothGatt getGatt(String address) {

        if (mGattHashMap == null || mGattHashMap.size() == 0) {
            return null;
        }
        if (mGattHashMap.containsKey(address)) {

            return mGattHashMap.get(address);
        }
        return null;
    }

    /**
     * 断开所有的设备连接
     */
    public void disconnectAll() {

        for (Map.Entry<String, BluetoothGatt> gattEntry : mGattHashMap.entrySet()) {
            String key = gattEntry.getKey();
            BluetoothGatt gatt = gattEntry.getValue();
            try {
                disconnect(key, gatt);
            } catch (Exception e) {
                BLELog.e("deviceAddress:" + key + " ; gatt is error");
            }

        }
    }

    /**
     * 断开某一个设备连接
     *
     * @param deviceAddress
     */
    public void disconnect(String deviceAddress) {

        BLELog.e("BLEConnectList :: disconnect() deviceAddress::" + deviceAddress);
        BluetoothGatt gatt = getGatt(deviceAddress);
        try {
            disconnect(deviceAddress, gatt);
        } catch (Exception e) {
            BLELog.e("deviceAddress:" + deviceAddress + " ; gatt is error");
        }

    }


    private final ThreadLocal<BluetoothAdapter> mBluetoothAdapter = new ThreadLocal<>();

    /**
     * 断开设备连接
     *
     * @param deviceAddress
     * @param gatt
     */
    private void disconnect(String deviceAddress, BluetoothGatt gatt) throws DeadObjectException {

        try {
            mBluetoothAdapter.set(BLESdk.get().getBluetoothAdapter());
            if (mBluetoothAdapter.get() == null) {
                return;
            }
            BLELog.e("disconnect() close gatt ::");

            BluetoothDevice bluetoothDevice = mBluetoothAdapter.get().getRemoteDevice(deviceAddress);

            if (bluetoothDevice == null) {
                return;
            }
            int state = BLESdk.get().getBluetoothManager().getConnectionState(bluetoothDevice, BluetoothProfile.GATT);
            if (state == BluetoothGatt.STATE_CONNECTED) {
                BLELog.e("disconnect()  gatt is connected ::  ");
                gatt.disconnect();
            }


//            mBluetoothAdapter.get().cancelDiscovery();
            removeGatt(deviceAddress);
            System.gc();

            BLELog.e("disconnect() close gatt :: finish ");
        } catch (Exception e) {
            e.printStackTrace();
            BLELog.e("deviceAddress:" + deviceAddress + " ; gatt is error");
        }
    }

    private void refreshCache(BluetoothGatt gatt) throws DeadObjectException {
//
//        try {
//            Method refresh = gatt.getClass().getMethod("refresh");
//            if (refresh != null) {
//                BLELog.e("refreshCache ::");
//                refresh.invoke(gatt);
//            }
//        } catch (Exception e) {
//            BLELog.e(e.getMessage());
//        }

        if (gatt == null) {
            return;
        }
        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod(
                    "refresh", new Class[0]);
            if (localMethod == null) {

                return;
            }

            localMethod.invoke(localBluetoothGatt, new Object[0]);

        } catch (Exception localException) {
            BLELog.e("An exception occured while refreshing device");
        }
    }

    public void removeGatt(String address) {
        if (mGattHashMap.containsKey(address)) {
            BLELog.e("removeGatt:: address");
            BluetoothGatt gatt = mGattHashMap.get(address);
            try {
                refreshCache(gatt);
                gatt.close();
            } catch (DeadObjectException e) {
                e.printStackTrace();
            }

            mGattHashMap.remove(address);
        }
        System.gc();
    }

    public void cleanGatt() {
        mGattHashMap.clear();
    }
}
