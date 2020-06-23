///*
// * Copyright (c) 2017. xiaoyunfei
// *
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *     you may not use this file except in compliance with the License.
// *     You may obtain a copy of the License at
// *
// *         http://www.apache.org/licenses/LICENSE-2.0
// *
// *     Unless required by applicable law or agreed to in writing, software
// *     distributed under the License is distributed on an "AS IS" BASIS,
// *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *     See the License for the specific language governing permissions and
// *     limitations under the License.
// */
//
//package com.e.ble.control;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.content.Context;
//
//import com.e.ble.BLESdk;
//import com.e.ble.control.listener.BLEConnListener;
//import com.e.ble.util.BLELog;
//
//import java.lang.reflect.Method;
//
///**
// * @package_name com.e.ble.control
// * @name ${name}
// * <p>
// * Created by xiaoyunfei on 2017/11/16.
// * @description
// */
//
//public class GattClient {
//
//    private String mAddress ;
//    private BluetoothGatt mGatt;
//    private BluetoothAdapter mBluetoothAdapter;
//    private BluetoothDevice mBluetoothDevice;
//    private GattCallback mGattCallback;
//
//    public GattClient(String address, BLEConnListener connListener) {
//        this.mAddress = address;
//        mGattCallback = new GattCallback(address);
//        mBluetoothAdapter = BLESdk.get().getBluetoothAdapter();
//        if (mBluetoothAdapter != null) {
//            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
//        }
//    }
//
//
//    public void connect(Context context, BluetoothGattCallback gattCallback) {
//
//        if (mGatt != null) {
//            mGatt.disconnect();
//            mGatt.close();
//        }
//        mGatt = mBluetoothDevice.connectGatt(context, false, gattCallback);
//        refreshDeviceCache(mGatt);
//    }
//
//    /**
//     * Attempt to invalidate Androids internal GATT table cache
//     *
//     * http://stackoverflow.com/a/22709467/5640435
//     *
//     * @param gatt BluetoothGatt object
//     */
//    private void refreshDeviceCache(BluetoothGatt gatt) {
//        try {
//            BluetoothGatt localBluetoothGatt = gatt;
//            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
//            if (localMethod != null) {
//                localMethod.invoke(localBluetoothGatt, new Object[0]);
//            } else {
//               BLELog.e("Couldn't find local method: refresh");
//            }
//        }
//        catch (Exception localException) {
//            BLELog.e("An exception occurred while refreshing device");
//        }
//    }
//
//}
