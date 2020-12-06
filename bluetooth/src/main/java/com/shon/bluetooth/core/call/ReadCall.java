package com.shon.bluetooth.core.call;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.shon.bluetooth.core.callback.ReadCallback;
import com.shon.bluetooth.util.BleLog;

import java.util.UUID;

/**
 *
 */
public final class ReadCall extends BaseCall<ReadCallback,ReadCall> {

    public ReadCall(String address) {
        super(address);
    }

    public void startRead(){
        BluetoothGatt gatt = bluetoothGatt();
        if (gatt == null) {
            BleLog.e("ReadCall gatt is null");
            return;
        }
        BluetoothGattCharacteristic characteristic = gattCharacteristic(gatt);
        if (characteristic == null) {
            BleLog.e("ReadCall gatt is null");
            return;
        }
        startTimer();
        boolean readCharacteristic = gatt.readCharacteristic(characteristic);
        BleLog.d("readCharacteristic =  " + readCharacteristic);
    }

    public UUID getCharacteristicUUID() {
        return characteristicUUID;
    }
}
