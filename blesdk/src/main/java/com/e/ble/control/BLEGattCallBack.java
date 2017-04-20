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

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.text.TextUtils;

import com.e.ble.bean.BLECharacter;
import com.e.ble.bean.BLEConnBean;
import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLEError;
import com.e.ble.util.BLELog;

import java.util.UUID;

/**
 * @package_name com.e.ble.control
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/4/15.
 * @description BluetoothGattCallback
 */

class BLEGattCallBack extends BluetoothGattCallback {

    /**
     * 设备连接状态的改变
     *
     * @param gatt
     * @param status
     * @param newState
     */
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

        //返回蓝牙连接状态
        updateConnectionState(gatt, status, newState);
        super.onConnectionStateChange(gatt, status, newState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {


        //蓝牙 设备所包含的 service 可被启用状态，
        // 注意
        // 如果蓝牙回复数据是通过notify的方式
        // 此时，发数据给设备并不能接收到设备，
        // 必须先 ENABLE_NOTIFICATION_VALUE，才可用

        String address = getConnectDevice(gatt);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            BLEConnection.get().onConnSuccess(address);
        } else {
            BLEConnection.get().onConnError(address, status);
        }
        super.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {


        if (status == BluetoothGatt.GATT_SUCCESS) {
            BLECharacter bleCharacter = getBleCharacter(gatt, characteristic);
            BLETransport.get().onCharacterRead(bleCharacter);
        }
        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic,
                                      int status) {

        if (status == BluetoothGatt.GATT_SUCCESS) {
            BLECharacter bleCharacter = getBleCharacter(gatt, characteristic);

            BLETransport.get().onCharacterWrite(bleCharacter);
        }
        super.onCharacteristicWrite(gatt, characteristic, status);

    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {

        BLECharacter bleCharacter = getBleCharacter(gatt, characteristic);
        BLETransport.get().onCharacterNotify(bleCharacter);
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt,
                                 BluetoothGattDescriptor descriptor,
                                 int status) {

        String address = getConnectDevice(gatt);
        BLETransport.get().onDesRead(address);
        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt,
                                  BluetoothGattDescriptor descriptor,
                                  int status) {

        String address = getConnectDevice(gatt);
        BLETransport.get().onDesWrite(address);
        UUID uuid = descriptor.getUuid();
        byte[] bytes =descriptor.getValue();
        BLELog.i(" onDescriptorWrite() address::" + address +
                "\n status:" + status +
                "\n value:" + BLEByteUtil.getHexString(bytes) +
                "\n uuid:" + uuid.toString());
        super.onDescriptorWrite(gatt, descriptor, status);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {

        String address = getConnectDevice(gatt);
        BLELog.e("onReliableWriteCompleted() address::" + address + ";status:" + status);
        super.onReliableWriteCompleted(gatt, status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

        String address = getConnectDevice(gatt);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            BLEControl.get().onReadRssi(address, rssi);
        } else {
            BLEControl.get().onReadRssiError(address, status);
        }
        super.onReadRemoteRssi(gatt, rssi, status);
    }

    //--------------------------------------------------------------------------------------------//
    //-------------------BLEGattCallBack 内部逻辑代码-----------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    /**
     * 更新与设备的连接状态，
     * GATT_SUCCESS 时调用gatt.discoverServices()，
     * 不返会设备连接成功，当discoverServices 成功时返回连接成功
     *
     * @param gatt
     * @param status
     * @param newState
     */
    private void updateConnectionState(BluetoothGatt gatt, int status, int newState) {

        String address = getConnectDevice(gatt);
        if (TextUtils.isEmpty(address)) {
            BLEConnection.get().onConnError(address, 0);
        }
        BLEConnBean connBean = BLEConnDeviceUtil.get().getExistBean(address);
        if (connBean == null) {
            return;
        }
        long startConnectTime = connBean.getStartConnTime();
        BLELog.i("BLEGattCallBack :: updateConnectionState()" +
                "\n address: " + address +
                "\n status == " + status +
                "\n newState == " + newState);


        if (status != BluetoothGatt.GATT_SUCCESS) {
            long disconncetTime = System.nanoTime();
            long offset = ((Math.abs(-disconncetTime)) / 1000000);
            if (offset <= 10) {
                BLEConnection.get().onConnError(address, BLEError.BLE_DEVICE_ERROR);
                return;
            }
            //蓝牙异常断开
            BLEConnection.get().onConnError(address, status);
            BLEConnection.get().onStateDisConnected(address);
            return;
        }

        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            long disconncetTime = System.nanoTime();
            long offset = ((Math.abs(-disconncetTime)) / 1000000);
            if (offset <= 10) {
                BLEConnection.get().onConnError(address, BLEError.BLE_DEVICE_ERROR);
                return;
            }
            BLEConnection.get().onStateDisConnected(address);
        }

        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            BLEConnection.get().onStateDisConnecting(address);
        }

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            //已连接成功
            gatt.discoverServices();
            BLEConnection.get().onStateConnected(address);
        }

        if (newState == BluetoothProfile.STATE_CONNECTING) {
            BLEConnection.get().onStateConnecting(address);
        }
    }

    /**
     * 从连接的 BluetoothGatt 中获取当前设备的 mac 地址
     *
     * @param gatt
     * @return
     */
    private synchronized String getConnectDevice(BluetoothGatt gatt) {
        String address = "";
        BluetoothDevice device = gatt.getDevice();
        if (device != null) {
            address = device.getAddress();
        }
        return address;
    }


    /**
     * 组装 BLECharacter
     *
     * @param gatt
     * @param characteristic
     * @return
     */
    private synchronized BLECharacter getBleCharacter(BluetoothGatt gatt,
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
}
