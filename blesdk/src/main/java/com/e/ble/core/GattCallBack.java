package com.e.ble.core;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnStateChangeListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 13:53
 * Package name : com.e.ble.core
 * Des :
 */
final class GattCallBack extends BluetoothGattCallback {

    private static GattCallBack gattCallBack;
    private StateChangedImpl stateChanged;
    private CharacteristicImpl characteristicImpl;
    private BluetoothGatt bluetoothGatt;

    public static GattCallBack gattCallBack() {
        if (gattCallBack == null) {
            synchronized (GattCallBack.class) {
                if (gattCallBack == null) {
                    gattCallBack = new GattCallBack();

                }
            }
        }
        return gattCallBack;
    }

    public GattCallBack() {
        super();

    }

    public boolean addConnectBean(ConnectBean connectBean) {

        if (stateChanged == null) {
            stateChanged = new StateChangedImpl(connectBean);
            return true;
        } else {
            return stateChanged.addConnectBean(connectBean);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        if (stateChanged == null) {
            stateChanged = new StateChangedImpl();
        }
        stateChanged.setOnStateChangeListener(onStateChangeListener);
    }

    public void setReadListener(OnReadMessage onReadMessage) {
        if (characteristicImpl == null) {
            characteristicImpl = new CharacteristicImpl();
        }
        characteristicImpl.setOnReadListener(onReadMessage);
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    private void updateBluetoothGatt(BluetoothGatt gatt) {
        bluetoothGatt = gatt;
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (stateChanged != null) {
            stateChanged.onConnectionStateChange(gatt, status, newState);
        }
        super.onConnectionStateChange(gatt, status, newState);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (stateChanged != null) {
            stateChanged.onServicesDiscovered(gatt, status);
        }
        super.onServicesDiscovered(gatt, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        if (characteristicImpl != null){
            characteristicImpl.onCharacteristicRead(gatt, characteristic, status);
        }
        super.onCharacteristicRead(gatt, characteristic, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        if (characteristicImpl != null){
            characteristicImpl.onCharacteristicWrite(gatt, characteristic, status);
        }

        super.onCharacteristicWrite(gatt, characteristic, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (characteristicImpl != null){
            characteristicImpl.onCharacteristicChanged(gatt, characteristic);
        }
        super.onCharacteristicChanged(gatt, characteristic);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        updateBluetoothGatt(gatt);
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyRead(gatt, txPhy, rxPhy, status);
        updateBluetoothGatt(gatt);
    }


}
