package com.e.ble.core;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Looper;

import com.e.ble.core.bean.ReadMessage;
import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLELog;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:52
 * Package name : com.e.ble.core
 * Des :
 */
class CharacteristicImpl {
    private OnReadMessage onReadMessage;
    private Handler handler;


    CharacteristicImpl() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public void setOnReadListener(OnReadMessage onReadMessage) {
        this.onReadMessage = onReadMessage;
    }

    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }

        BLELog.e("CharacteristicImpl -->> onCharacteristicRead :: " + BLEByteUtil.getHexString(characteristic.getValue()));
        if (onReadMessage == null) {

            return;
        }

        ReadMessage readMessage = new ReadMessage();

        readMessage.setServiceUUID(characteristic.getService().getUuid());
        readMessage.setCharacteristicUUID(characteristic.getUuid());
        readMessage.setAddress(gatt.getDevice().getAddress());
        readMessage.setBytes(characteristic.getValue());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onReadMessage.onReadMessage(readMessage);
            }
        });


    }

    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

    }


}
