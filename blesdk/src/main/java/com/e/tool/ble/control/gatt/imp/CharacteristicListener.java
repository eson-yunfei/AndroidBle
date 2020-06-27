package com.e.tool.ble.control.gatt.imp;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.e.tool.ble.bean.message.NotifyState;
import com.e.tool.ble.imp.OnDataNotify;
import com.e.tool.ble.imp.OnRead;
import com.e.tool.ble.imp.OnWriteDescriptor;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 19:14
 * Package name : com.e.tool.ble.control.gatt.imp
 * Des :
 */
public interface CharacteristicListener {

    void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

    void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

    void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);

    void setWriteDescriptor(NotifyState notifyState, OnWriteDescriptor writeDescriptor);

    void setDataNotifyListener(OnDataNotify onDataNotify);

    void setOnReadListener(OnRead readMessageListener);

}
