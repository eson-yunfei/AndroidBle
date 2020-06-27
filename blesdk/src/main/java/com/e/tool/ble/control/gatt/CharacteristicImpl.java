package com.e.tool.ble.control.gatt;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.e.tool.ble.bean.message.NotifyMessage;
import com.e.tool.ble.bean.message.NotifyState;
import com.e.tool.ble.bean.message.ReadMessage;
import com.e.tool.ble.control.gatt.imp.CharacteristicListener;
import com.e.tool.ble.imp.OnDataNotify;
import com.e.tool.ble.imp.OnRead;
import com.e.tool.ble.imp.OnWriteDescriptor;
import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLELog;

import java.util.Arrays;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:52
 * Package name : com.e.ble.core
 * Des :
 */
class CharacteristicImpl implements CharacteristicListener {
    private OnRead onRead;
    private NotifyState notifyState;
    private OnWriteDescriptor onWriteDescriptor;

    private OnDataNotify onDataNotify;
    private Handler handler;


    CharacteristicImpl() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }



    public void setOnReadListener(OnRead onRead) {
        this.onRead = onRead;
    }

    public void setWriteDescriptor(NotifyState notifyState, OnWriteDescriptor onWriteDescriptor) {
        this.notifyState = notifyState;
        this.onWriteDescriptor = onWriteDescriptor;

    }

    public void setDataNotifyListener(OnDataNotify onDataNotify) {
        this.onDataNotify = onDataNotify;
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }

        BLELog.e("CharacteristicImpl -->> onCharacteristicRead :: " + BLEByteUtil.getHexString(characteristic.getValue()));
        if (onRead == null) {

            return;
        }

        ReadMessage readMessage = new ReadMessage();

        readMessage.setServiceUUID(characteristic.getService().getUuid());
        readMessage.setCharacteristicUUID(characteristic.getUuid());
        readMessage.setAddress(gatt.getDevice().getAddress());
        readMessage.setBytes(characteristic.getValue());
        handler.post(() -> onRead.onReadMessage(readMessage));

    }


    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
        //TODO call back send success
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        if (onDataNotify == null){
            return;
        }
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setAddress(gatt.getDevice().getAddress());
        notifyMessage.setBytes(characteristic.getValue());
        notifyMessage.setServiceUUID(characteristic.getService().getUuid());
        notifyMessage.setCharacteristicUUID(characteristic.getUuid());
        onDataNotify.onDataNotify(notifyMessage);
    }


    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {


        NotifyState result = new NotifyState();

        result.setServiceUUID(descriptor.getUuid());
        result.setCharacteristicUUID(descriptor.getCharacteristic().getUuid());
        result.setAddress(gatt.getDevice().getAddress());

        String value = Arrays.toString(descriptor.getValue());
        if (notifyState.isEnable()) {
            boolean ret = TextUtils.equals(value, Arrays.toString(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
            result.setResult(ret);
        } else {
            boolean ret = TextUtils.equals(value, Arrays.toString(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE));
            result.setResult(ret);
        }

        handler.post(() -> {
            if (onWriteDescriptor != null) {
                onWriteDescriptor.onWriteDescriptor(result);
            }
        });

    }


}
