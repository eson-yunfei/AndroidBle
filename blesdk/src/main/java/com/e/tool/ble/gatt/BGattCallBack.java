package com.e.tool.ble.gatt;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.e.tool.ble.gatt.imp.CharacteristicListener;
import com.e.tool.ble.gatt.imp.StateChangeListener;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 18:46
 * Package name : com.e.tool.ble.gatt
 * Des :
 */
public class BGattCallBack extends BluetoothGattCallback {

    protected CharacteristicImpl characteristicListener;
    protected StateChangedImpl stateChangeListener;

    public void setCharacteristicImpl() {
        this.characteristicListener = new CharacteristicImpl();
    }

    public void setStateChangeImpl() {
        stateChangeListener = new StateChangedImpl();
    }

    public CharacteristicListener getCharacteristicListener() {
        return characteristicListener;
    }

    public StateChangeListener getStateChangeListener() {
        return stateChangeListener;
    }

    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        stateChangeListener.onConnectionStateChange(gatt, status, newState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        stateChangeListener.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        characteristicListener.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        characteristicListener.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        characteristicListener.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
    }


}
