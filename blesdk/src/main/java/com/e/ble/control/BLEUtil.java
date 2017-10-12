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
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.os.DeadObjectException;

import com.e.ble.BLESdk;
import com.e.ble.bean.BLECharacter;
import com.e.ble.bean.BLEConnBean;
import com.e.ble.util.BLELog;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * @package_name com.e.ble.control
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/4/21.
 * @description
 */

class BLEUtil {

    public static boolean isConnected(String address) {
        BluetoothManager manager = BLESdk.get().getBluetoothManager();
        if (manager == null) {
            return false;
        }
//        List<BluetoothDevice> bindList = getBindDevices();
//        if (bindList == null) {
//            return false;
//        }
////        BLELog.i("--->>>bindList::" + bindList.size());
//
//        BluetoothDevice device = null;
//        for (BluetoothDevice bluetoothDevice : bindList) {
//            String bindDevice = bluetoothDevice.getAddress();
//
//            if (TextUtils.equals(bindDevice, address)) {
//                device = bluetoothDevice;
////                BLELog.i("--->>>contain bindDevice::" + bindDevice);
//            }
//        }
//        if (device == null) {
//            return false;
//        }
        BluetoothAdapter adapter = BLESdk.get().getBluetoothAdapter();
        if (adapter == null) {
            return false;
        }

        BluetoothDevice device = adapter.getRemoteDevice(address);
        if (device == null) {
            return false;
        }

        int state = manager.getConnectionState(device, BluetoothProfile.GATT);
//        BLELog.i("--->>> " + address + " " + getStateString(state));
        return state == BluetoothProfile.STATE_CONNECTED;
    }

    public static List<BluetoothDevice> getBindDevices() {

        BluetoothManager manager = BLESdk.get().getBluetoothManager();
        if (manager == null) {
            return null;
        }

        return manager.getConnectedDevices(BluetoothProfile.GATT_SERVER);
    }


    public static void disConnect(String address, BluetoothGatt gatt) {

        BluetoothAdapter adapter = BLESdk.get().getBluetoothAdapter();
        if (adapter == null || !adapter.isEnabled() || gatt == null) {
            return;
        }
        if (isConnected(address)) {
            gatt.disconnect();
        }
        try {
            refreshCache(gatt);
        } catch (DeadObjectException e) {
            e.printStackTrace();
        }

        gatt.close();
    }


    public static BluetoothGatt getBluetoothGatt(String address) {
        BLEConnBean connBean = BLEConnList.get().getContainBean(address);
        if (connBean == null) {
            return null;
        }
        return connBean.getBluetoothGatt();
    }


    private static void refreshCache(BluetoothGatt gatt) throws DeadObjectException {

        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod(
                    "refresh");
            if (localMethod == null) {

                return;
            }

            localMethod.invoke(localBluetoothGatt);

        } catch (Exception localException) {
            BLELog.e("An exception occured while refreshing device");
        }
    }

    /**
     * 组装 BLECharacter
     *
     * @param gatt
     * @param characteristic
     * @return
     */
    public static BLECharacter getBleCharacter(BluetoothGatt gatt,
                                               BluetoothGattCharacteristic characteristic) {
        String address = getConnectDevice(gatt);
        UUID uuid = characteristic.getUuid();
        byte[] value = characteristic.getValue();

        BLECharacter.BLECharacterBuilder bleCharacterBuilder =
                new BLECharacter.BLECharacterBuilder(value);
        return bleCharacterBuilder
                .setDeviceAddress(address)
                .setCharacteristicUUID(uuid).builder();
    }

    /**
     * 从连接的 BluetoothGatt 中获取当前设备的 mac 地址
     *
     * @param gatt
     * @return
     */
    public static String getConnectDevice(BluetoothGatt gatt) {
        String address = "";
        if (gatt == null) {
            return address;
        }
        BluetoothDevice device = gatt.getDevice();
        if (device != null) {
            address = device.getAddress();
        }
        return address;
    }


    private static String getStateString(int state) {
        String message = "";
        switch (state) {
            case BluetoothProfile.STATE_CONNECTED:
                message = "已连接";
                break;
            case BluetoothProfile.STATE_CONNECTING:
                message = "正在连接";
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                message = "已断开连接";
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                message = "正在断开连接";
                break;
        }
        return message;
    }

}
