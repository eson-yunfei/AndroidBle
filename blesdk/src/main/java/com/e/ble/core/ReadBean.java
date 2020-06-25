package com.e.ble.core;

import android.bluetooth.BluetoothGatt;

import com.e.ble.core.bean.ReadMessage;
import com.e.ble.core.imp.OnReadMessage;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 15:34
 * Package name : com.e.ble.core
 * Des :
 */
class ReadBean {

    boolean isWaiting = true;
    private ReadMessage readBean;
    private OnReadMessage onReadMessage;
    private BluetoothGatt gatt;


    public ReadBean(ReadMessage readMessage, BluetoothGatt gatt, OnReadMessage onReadMessage) {

        this.readBean = readMessage;
        this.gatt = gatt;
        this.onReadMessage = onReadMessage;
    }

    public void onReadError() {
        setWaiting(false);
        onReadMessage.onReadError();
    }

    public void onReadMessage(ReadMessage readMessage) {
        setWaiting(false);
        onReadMessage.onReadMessage(readMessage);
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

    public OnReadMessage getOnReadMessage() {
        return onReadMessage;
    }


}
