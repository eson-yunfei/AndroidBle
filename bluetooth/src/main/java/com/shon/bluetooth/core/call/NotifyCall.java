package com.shon.bluetooth.core.call;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.shon.bluetooth.core.callback.NotifyCallback;
import com.shon.bluetooth.util.BleLog;

import java.util.UUID;

public final class NotifyCall extends BaseCall<NotifyCallback,NotifyCall> {
    public NotifyCall(String address) {
        super(address);
    }

    public void changeSate() {
        boolean enable = callBack.getTargetSate();
        BluetoothGatt gatt = bluetoothGatt();
        if (gatt == null) {
            BleLog.e("NotifyCall gatt is null");
            return;
        }
        BluetoothGattCharacteristic characteristic = gattCharacteristic(gatt);
        if (characteristic == null) {
            BleLog.e("NotifyCall gatt is null");
            return;
        }
        startTimer();
        gatt.setCharacteristicNotification(characteristic, enable);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(callBack.getDescriptor()));
        if (enable) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        boolean writeDescriptor = gatt.writeDescriptor(descriptor);
        BleLog.d("changeSate() writeDescriptor  = " + writeDescriptor);
    }
}
