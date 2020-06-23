package com.e.ble.core;

import android.bluetooth.BluetoothGatt;

import com.e.ble.core.imp.OnConnectListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:43
 * Package name : com.e.ble.core
 * Des :
 */
class ConnectBean {
    private String address;
    private OnConnectListener connListener;
    private BluetoothGatt gatt;

    public ConnectBean(String address, OnConnectListener connListener) {

    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public BluetoothGatt getGatt() {
        return gatt;
    }
    public String getAddress() {
        return address;
    }

    public OnConnectListener getConnListener() {
        return connListener;
    }
}
