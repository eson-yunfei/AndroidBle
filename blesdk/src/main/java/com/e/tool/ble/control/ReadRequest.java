package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.e.tool.ble.bean.message.ReadMessage;
import com.e.tool.ble.control.request.Request;
import com.e.tool.ble.imp.OnRead;

import java.util.UUID;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 15:34
 * Package name : com.e.ble.core
 * Des :
 */
class ReadRequest extends Request {

    boolean isWaiting = true;
    private ReadMessage readBean;
    private OnRead onRead;
    private BluetoothGatt gatt;


    public ReadRequest(ReadMessage readMessage, BluetoothGatt gatt, OnRead onRead) {

        this.readBean = readMessage;
        this.gatt = gatt;
        this.onRead = onRead;
    }

    @Override
    public boolean launch() {

        UUID serviceUuid = readBean.getServiceUUID();
        UUID characteristicUuid = readBean.getCharacteristicUUID();

        BluetoothGattCharacteristic characteristic =
                getCharacteristicByUUID(gatt,
                        serviceUuid,
                        characteristicUuid);
        if (characteristic == null) {
            onRead.onReadError();
            return false;
        }
        if (gatt.readCharacteristic(characteristic)) {
            return true;
        } else {
            onRead.onReadError();
            return false;
        }

    }

    public void onReadError() {
        setWaiting(false);
        onRead.onReadError();
    }

    public void onReadMessage(ReadMessage readMessage) {
        setWaiting(false);
        onRead.onReadMessage(readMessage);
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public ReadMessage getReadBean() {
        return readBean;
    }

    public BluetoothGatt getGatt() {
        return gatt;
    }

    public OnRead getOnRead() {
        return onRead;
    }


    /**
     * 获取指定的 GattCharacteristic
     *
     * @param serviceUuid
     * @param characteristicUuid
     * @return
     */
    private BluetoothGattCharacteristic getCharacteristicByUUID(
            BluetoothGatt bluetoothGatt,
            UUID serviceUuid,
            UUID characteristicUuid) {
        BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(characteristicUuid);
    }

}
