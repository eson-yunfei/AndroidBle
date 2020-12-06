package com.shon.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.shon.bluetooth.core.gatt.CharacteristicGattCallback;
import com.shon.bluetooth.core.gatt.ConnectionGattCallback;
import com.shon.bluetooth.core.gatt.RssiGattCallback;

/**
 * Auth : xiao.yunfei
 * Date : 2020/09/27 14:16
 * Package name : com.shon.bluetooth.contorller
 * Des :
 */
class GattCallback extends BluetoothGattCallback {
    private final ConnectionGattCallback connectionGattCallback;
    private final CharacteristicGattCallback characteristicGattCallback;
    private final RssiGattCallback rssiGattCallback;

     GattCallback(ConnectDispatcher connectDispatcher,DataDispatcher dataDispatcher) {
         connectionGattCallback = new ConnectionGattCallback(connectDispatcher);
         characteristicGattCallback = new CharacteristicGattCallback(dataDispatcher);
         rssiGattCallback = new RssiGattCallback(dataDispatcher);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        connectionGattCallback.onConnectionStateChange(gatt, status, newState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        connectionGattCallback.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        characteristicGattCallback.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        characteristicGattCallback.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        characteristicGattCallback.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        characteristicGattCallback.onDescriptorWrite(gatt, descriptor, status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        rssiGattCallback.onReadRemoteRssi(gatt, rssi, status);
    }


}
