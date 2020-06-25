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
//import android.bluetooth.BluetoothProfile;
//import android.os.Handler;
//import android.text.TextUtils;
//
//import com.e.ble.bean.BLECharacter;
//import com.e.ble.bean.BLEConnBean;
//import com.e.ble.util.BLEByteUtil;
//import com.e.ble.util.BLELog;
//
//import java.util.UUID;
//
///**
// * @package_name com.e.ble.control
// * @name ${name}
// * <p>
// * Created by xiaoyunfei on 2017/4/15.
// * @description BluetoothGattCallback
// */
//
//class BLEGattCallBack extends BluetoothGattCallback {
//
//    private Handler mHandler = new Handler();
//
//    /**
//     * 设备连接状态的改变
//     *
//     * @param gatt
//     * @param status
//     * @param newState
//     */
//    @Override
//    public void onConnectionStateChange(BluetoothGatt gatt,
//                                        int status,
//                                        int newState) {
//
//        //返回蓝牙连接状态
//        updateConnectionState(gatt, status, newState);
//
//        super.onConnectionStateChange(gatt, status, newState);
//    }
//
//    @Override
//    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//
//        //蓝牙 设备所包含的 service 可被启用状态，
//        // 注意:
//        // 如果蓝牙回复数据是通过notify的方式
//        // 此时，发数据给设备并不能接收到设备，
//        // 必须先 ENABLE_NOTIFICATION_VALUE，才可用
//
//        String address = BLEUtil.getConnectDevice(gatt);
//        if (status == BluetoothGatt.GATT_SUCCESS) {
//            BLEConnection.get().onConnSuccess(address);
//        } else {
//            BLEConnection.get().onConnError(address, status);
//        }
//        super.onServicesDiscovered(gatt, status);
//    }
//
//    @Override
//    public void onCharacteristicRead(final BluetoothGatt gatt,
//                                     final BluetoothGattCharacteristic characteristic,
//                                     final int status) {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    BLECharacter bleCharacter = BLEUtil.getBleCharacter(gatt, characteristic);
//                    BLETransport.get().onCharacterRead(bleCharacter);
//                }
//            }
//        });
//
//        super.onCharacteristicRead(gatt, characteristic, status);
//    }
//
//    @Override
//    public void onCharacteristicWrite(BluetoothGatt gatt,
//                                      BluetoothGattCharacteristic characteristic,
//                                      int status) {
//
//        if (status == BluetoothGatt.GATT_SUCCESS) {
//            BLECharacter bleCharacter = BLEUtil.getBleCharacter(gatt, characteristic);
////            if (bleCharacter != null) {
////                BLELog.i("----->>>BLEGattCallBack::" +
////                        "\nonCharacteristicWrite::  "
////                        + BLEByteUtil.getHexString(bleCharacter.getDataBuffer()));
////            }
//            BLETransport.get().onCharacterWrite(bleCharacter);
//        }
//        super.onCharacteristicWrite(gatt, characteristic, status);
//
//    }
//
//    @Override
//    public void onCharacteristicChanged(final BluetoothGatt gatt,
//                                        final BluetoothGattCharacteristic characteristic) {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                BLECharacter bleCharacter = BLEUtil.getBleCharacter(gatt, characteristic);
//                BLETransport.get().onCharacterNotify(bleCharacter);
//            }
//        });
//
//        super.onCharacteristicChanged(gatt, characteristic);
//    }
//
//    @Override
//    public void onDescriptorRead(final BluetoothGatt gatt,
//                                 BluetoothGattDescriptor descriptor,
//                                 int status) {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                String address = BLEUtil.getConnectDevice(gatt);
//                BLETransport.get().onDesRead(address);
//            }
//        });
//
//        super.onDescriptorRead(gatt, descriptor, status);
//    }
//
//    @Override
//    public void onDescriptorWrite(final BluetoothGatt gatt,
//                                  final BluetoothGattDescriptor descriptor,
//                                  final int status) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                String address = BLEUtil.getConnectDevice(gatt);
//                BLETransport.get().onDesWrite(address);
//                UUID uuid = descriptor.getUuid();
//                byte[] bytes = descriptor.getValue();
//                BLELog.i(" onDescriptorWrite() address::" + address +
//                        "\n status:" + status +
//                        "\n value:" + BLEByteUtil.getHexString(bytes) +
//                        "\n uuid:" + uuid.toString());
//            }
//        });
//
//        super.onDescriptorWrite(gatt, descriptor, status);
//    }
//
//    @Override
//    public void onReliableWriteCompleted(final BluetoothGatt gatt,
//                                         final int status) {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                String address = BLEUtil.getConnectDevice(gatt);
//                BLELog.e("onReliableWriteCompleted() address::" + address + ";status:" + status);
//            }
//        });
//
//        super.onReliableWriteCompleted(gatt, status);
//    }
//
//    @Override
//    public void onReadRemoteRssi(final BluetoothGatt gatt,
//                                 final int rssi,
//                                 final int status) {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                String address = BLEUtil.getConnectDevice(gatt);
//                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    BLEControl.get().onReadRssi(address, rssi);
//                } else {
//                    BLEControl.get().onReadRssiError(address, status);
//                }
//            }
//        });
//
//        super.onReadRemoteRssi(gatt, rssi, status);
//    }
//
//    //--------------------------------------------------------------------------------------------//
//    //-------------------BLEGattCallBack 内部逻辑代码-----------------------------------------------//
//    //--------------------------------------------------------------------------------------------//
//
//    /**
//     * 更新与设备的连接状态，
//     * GATT_SUCCESS 时调用gatt.discoverServices()，
//     * 不返会设备连接成功，当discoverServices 成功时返回连接成功
//     *
//     * @param gatt
//     * @param status
//     * @param newState
//     */
//    private void updateConnectionState(BluetoothGatt gatt, int status, int newState) {
//
//        String address = BLEUtil.getConnectDevice(gatt);
//        if (TextUtils.isEmpty(address)) {
//            BLEConnection.get().onConnError(address, 0);
//        }
//        BLEConnBean connBean = BLEConnList.get().getContainBean(address);
//        if (connBean == null) {
//            return;
//        }
//        BLELog.i("BLEGattCallBack :: updateConnectionState()" +
//                "\n address: " + address +
//                "\n status == " + status +
//                "\n newState == " + newState);
//
//
//        if (status != BluetoothGatt.GATT_SUCCESS) {
//            //蓝牙异常断开
//            BLEConnection.get().onConnError(address, status);
//            BLEConnection.get().onStateDisConnected(address);
//            BLEConnList.get().delConnDevice(address);
//            return;
//        }
//
//        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//            BLEConnection.get().onStateDisConnected(address);
//            BLEConnList.get().delConnDevice(address);
//
//        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//            BLEConnection.get().onStateDisConnecting(address);
//        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
//            //已连接成功
//            gatt.discoverServices();
//            BLEConnection.get().onStateConnected(address);
//            return;
//        } else if (newState == BluetoothProfile.STATE_CONNECTING) {
//            BLEConnection.get().onStateConnecting(address);
//        }
//    }
//
//}
