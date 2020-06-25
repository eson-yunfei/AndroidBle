package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;

import com.e.tool.ble.bean.ReadMessage;
import com.e.tool.ble.imp.OnRead;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 15:34
 * Package name : com.e.ble.core
 * Des :
 */
class ReadBean {

    boolean isWaiting = true;
    private ReadMessage readBean;
    private OnRead onRead;
    private BluetoothGatt gatt;


    public ReadBean(ReadMessage readMessage, BluetoothGatt gatt, OnRead onRead) {

        this.readBean = readMessage;
        this.gatt = gatt;
        this.onRead = onRead;
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


}
