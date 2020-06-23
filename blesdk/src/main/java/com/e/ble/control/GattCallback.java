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
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//
///**
// * @package_name com.e.ble.control
// * @name ${name}
// * <p>
// * Created by xiaoyunfei on 2017/11/16.
// * @description
// */
//
//final class GattCallback extends BluetoothGattCallback {
//    private String mAddress;
//
//    public GattCallback(String address) {
//        this.mAddress = address;
//    }
//
//    @Override
//    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//        super.onConnectionStateChange(gatt, status, newState);
//    }
//
//    @Override
//    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//        super.onServicesDiscovered(gatt, status);
//    }
//
//    @Override
//    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//        super.onCharacteristicRead(gatt, characteristic, status);
//    }
//
//    @Override
//    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//        super.onCharacteristicWrite(gatt, characteristic, status);
//    }
//
//    @Override
//    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//        super.onCharacteristicChanged(gatt, characteristic);
//    }
//
//    @Override
//    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//        super.onDescriptorRead(gatt, descriptor, status);
//    }
//
//    @Override
//    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//        super.onDescriptorWrite(gatt, descriptor, status);
//    }
//
//    @Override
//    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
//        super.onReliableWriteCompleted(gatt, status);
//    }
//
//    @Override
//    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//        super.onReadRemoteRssi(gatt, rssi, status);
//    }
//
//    @Override
//    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
//        super.onMtuChanged(gatt, mtu, status);
//    }
//}
