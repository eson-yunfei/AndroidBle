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
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothProfile;
//
//import com.e.ble.bean.BLECharacter;
//import com.e.ble.control.listener.BLEConnListener;
//import com.e.ble.control.listener.BLEReadRssiListener;
//import com.e.ble.control.listener.BLEStateChangeListener;
//import com.e.ble.control.listener.BLETransportListener;
//import com.e.ble.util.BLELog;
//
//import java.util.UUID;
//
///**
// * |---------------------------------------------------------------------------------------------------------------|
// *
// * @作者 xiaoyunfei
// * @日期: 2017/3/5
// * @说明： BaseControl  ，  提供唯一的 BluetoothGattCallback,
// * <p>
// * 根据设备 的 mac 地址 返回不同的数据
// * <p>
// * |---------------------------------------------------------------------------------------------------------------|
// */
//
//abstract class BaseControl implements BLEConnListener, BLETransportListener,
//        BLEStateChangeListener, BLEReadRssiListener {
//
//    @Override
//    public void onConnError(String address, int errorCode) {
//        BLEConnection.get().onConnError(address, errorCode);
//    }
//
//    @Override
//    public void onConnSuccess(String address) {
//        BLEConnection.get().onConnSuccess(address);
//    }
//
//
//    @Override
//    public void onAlreadyConnected(String address) {
//        BLEConnection.get().onAlreadyConnected(address);
//    }
//
//    @Override
//    public void onStateConnected(String address) {
//
//        BLEConnection.get().onStateConnected(address);
//    }
//
//    @Override
//    public void onStateConnecting(String address) {
//        BLEConnection.get().onStateConnecting(address);
//    }
//
//    @Override
//    public void onStateDisConnecting(String address) {
//        BLEConnection.get().onStateDisConnecting(address);
//    }
//
//    @Override
//    public void onStateDisConnected(String address) {
//    }
//
//    @Override
//    public void onDesRead(String address) {
//        BLETransport.get().onDesRead(address);
//    }
//
//
//    @Override
//    public void onDesWrite(String address) {
//        BLETransport.get().onDesWrite(address);
//    }
//
//    @Override
//    public void onCharacterRead(BLECharacter bleCharacter) {
//
//        BLETransport.get().onCharacterRead(bleCharacter);
//    }
//
//    @Override
//    public void onCharacterWrite(BLECharacter bleCharacter) {
//        BLETransport.get().onCharacterWrite(bleCharacter);
//    }
//
//    @Override
//    public void onCharacterNotify(BLECharacter bleCharacter) {
//        BLETransport.get().onCharacterNotify(bleCharacter);
//    }
//
//}
