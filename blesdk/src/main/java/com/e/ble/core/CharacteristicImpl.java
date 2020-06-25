package com.e.ble.core;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.e.ble.core.bean.NotifyState;
import com.e.ble.core.bean.ReadMessage;
import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnUpdateNotify;
import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLELog;

import java.util.Arrays;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:52
 * Package name : com.e.ble.core
 * Des :
 */
class CharacteristicImpl {
    private OnReadMessage onReadMessage;
    private NotifyState notifyState;
    private OnUpdateNotify onUpdateNotify;
    private Handler handler;


    CharacteristicImpl() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public void setOnReadListener(OnReadMessage onReadMessage) {
        this.onReadMessage = onReadMessage;
    }

    public void setWriteDescriptor(NotifyState notifyState, OnUpdateNotify onUpdateNotify) {
        this.notifyState = notifyState;
        this.onUpdateNotify = onUpdateNotify;

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
        handler.post(() -> onReadMessage.onReadMessage(readMessage));


    }

    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
        //TODO call back send success
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

    }


    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {


        NotifyState result = new NotifyState();

        result.setServiceUUID(descriptor.getUuid());
        result.setCharacteristicUUID(descriptor.getCharacteristic().getUuid());
        result.setAddress(gatt.getDevice().getAddress());

        String value = Arrays.toString(descriptor.getValue());
        if (notifyState.isEnable()) {
            boolean ret =  TextUtils.equals(value, Arrays.toString(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
            result.setResult(ret);
        }else {
            boolean ret =  TextUtils.equals(value, Arrays.toString(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE));
            result.setResult(ret);
        }

        handler.post(() -> {
            if (onUpdateNotify != null){
                onUpdateNotify.onWriteDescriptor(result);
            }
        });

    }

}
