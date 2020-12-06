package com.shon.bluetooth.core.call;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.shon.bluetooth.BLEManager;
import com.shon.bluetooth.core.callback.WriteCallback;
import com.shon.bluetooth.util.BleLog;
import com.shon.bluetooth.util.ByteUtil;

public final class WriteCall extends BaseCall<WriteCallback, WriteCall> {

    public WriteCall(String address) {
        super(address);
    }

    public void write(){
        BluetoothGatt gatt = bluetoothGatt();
        if (gatt == null) {
            BleLog.e("WriteCall gatt is null");
            return;
        }
        BluetoothGattCharacteristic characteristic = gattCharacteristic(gatt);
        if (characteristic == null) {
            BleLog.e("WriteCall characteristic is null");
            return;
        }


        byte[] sendData = callBack.getSendData();
        BleLog.d("writeInfo byte[] = " + ByteUtil.getHexString(sendData));
        boolean setValue = characteristic.setValue(sendData);
        if (setValue) {
            boolean writeCharacteristic = gatt.writeCharacteristic(characteristic);
            BleLog.d("Writer write()   writeCharacteristic  " + writeCharacteristic);
            if (writeCharacteristic){
                startTimer();
            }else {
                BLEManager.getInstance().getDataDispatcher().startSendNext(true);
            }
        }
    }
}
