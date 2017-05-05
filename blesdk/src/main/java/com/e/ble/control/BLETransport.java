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
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.e.ble.BLESdk;
import com.e.ble.bean.BLECharacter;
import com.e.ble.bean.BLEUuid;
import com.e.ble.control.listener.BLETransportListener;
import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLELog;

import java.util.UUID;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： BLETransport
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */

class BLETransport implements BLETransportListener {

    private static BLETransport bleTransport = null;
    private BLETransportListener bleTransportListener = null;

    private BLETransport() {
        ThreadLocal<BluetoothAdapter> bluetoothAdapter = new ThreadLocal<>();
        bluetoothAdapter.set(BLESdk.get().getBluetoothAdapter());
    }

    public static BLETransport get() {
        if (bleTransport == null) {
            bleTransport = new BLETransport();
        }
        return bleTransport;
    }


    /**
     * 设置回调
     *
     * @param bleTransportListener
     */
    public void setBleTransportListener(BLETransportListener bleTransportListener) {
        this.bleTransportListener = bleTransportListener;
    }

    /**
     * 发送数据
     *
     * @param bleUuid
     */
    public void sendDataToDevice(BLEUuid bleUuid) {

        BluetoothGatt bluetoothGatt = getEnableBleGatt(bleUuid);
        if (bluetoothGatt == null) {
            return;
        }

        UUID serviceUuid = bleUuid.getServiceUUID();
        UUID characteristicUuid = bleUuid.getCharacteristicUUID();
        BluetoothGattCharacteristic characteristic =
                getCharacteristicByUUID(bluetoothGatt, serviceUuid, characteristicUuid);
        if (characteristic == null) {
            return;
        }
        BLELog.e("发送数据-->>:");
        BLEByteUtil.printHex(bleUuid.getDataBuffer());
        characteristic.setValue(bleUuid.getDataBuffer());
        bluetoothGatt.writeCharacteristic(characteristic);
    }

    @Nullable
    private BluetoothGatt getEnableBleGatt(BLEUuid bleUuid) {
        String address = bleUuid.getAddress();
        if (TextUtils.isEmpty(address) || !BLEUtil.isConnected(address)) {
            return null;
        }

        BluetoothGatt bluetoothGatt = BLEUtil.getBluetoothGatt(address);
        return bluetoothGatt;
    }

    /**
     * 通知的启用和关闭
     *
     * @param bleUuid
     */
    public void enableNotify(BLEUuid bleUuid) {
        BluetoothGatt bluetoothGatt = getEnableBleGatt(bleUuid);
        if (bluetoothGatt == null) {
            return;
        }

        UUID serviceUuid = bleUuid.getServiceUUID();
        UUID characteristicUuid = bleUuid.getCharacteristicUUID();
        BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(bluetoothGatt,
                serviceUuid,
                characteristicUuid);
        if (characteristic == null) {
            return;
        }
        UUID descriptorUuid = bleUuid.getDescriptorUUID();
        changeNotifyState(bluetoothGatt,
                characteristic,
                descriptorUuid,
                bleUuid.isEnable());
        BLELog.e("enableNotify()-->>characteristicUuid:" + characteristicUuid);

    }

    /**
     * 读取数据
     *
     * @param bleUuid
     */
    public void readDeviceData(BLEUuid bleUuid) {
        BluetoothGatt bluetoothGatt = getEnableBleGatt(bleUuid);
        if (bluetoothGatt == null) {
            return;
        }
        UUID serviceUuid = bleUuid.getServiceUUID();
        UUID characteristicUuid = bleUuid.getCharacteristicUUID();

        BluetoothGattCharacteristic characteristic =
                getCharacteristicByUUID(bluetoothGatt,
                        serviceUuid,
                        characteristicUuid);
        if (characteristic == null) {
            return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }


    /**
     * 获取指定的 GattCharacteristic
     *
     * @param serviceUuid
     * @param characteristicUuid
     * @return
     */
    private BluetoothGattCharacteristic getCharacteristicByUUID(
            BluetoothGatt bluetoothGatt,
            UUID serviceUuid,
            UUID characteristicUuid) {
        BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(characteristicUuid);
    }

    /**
     * 改变通知状态
     *
     * @param characteristic
     * @param descriptorUuid
     * @param enable
     */
    private void changeNotifyState(BluetoothGatt bluetoothGatt,
                                   BluetoothGattCharacteristic characteristic,
                                   UUID descriptorUuid,
                                   boolean enable) {
        bluetoothGatt.setCharacteristicNotification(characteristic, enable);//激活通知

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUuid);
        if (descriptor == null) {
            BLELog.e("descriptorUuid not find");
            return;
        }
        if (enable) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        bluetoothGatt.writeDescriptor(descriptor);
    }


    @Override
    public void onCharacterRead(BLECharacter bleCharacter) {

        if (bleTransportListener != null) {
            bleTransportListener.onCharacterRead(bleCharacter);
        }
    }

    @Override
    public void onCharacterWrite(BLECharacter bleCharacter) {
        if (bleTransportListener != null) {
            bleTransportListener.onCharacterWrite(bleCharacter);
        }
    }

    @Override
    public void onCharacterNotify(BLECharacter bleCharacter) {
        BLELog.e("BLETransport -->>onCharacterNotify()");
        if (bleTransportListener != null) {
            bleTransportListener.onCharacterNotify(bleCharacter);
        }
    }

    @Override
    public void onDesRead(String address) {
        if (bleTransportListener != null) {
            bleTransportListener.onDesRead(address);
        }
    }

    @Override
    public void onDesWrite(String address) {

        if (bleTransportListener != null) {
            bleTransportListener.onDesWrite(address);
        }
    }
}
