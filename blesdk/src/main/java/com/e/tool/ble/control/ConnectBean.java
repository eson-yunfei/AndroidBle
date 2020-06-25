package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;

import com.e.tool.ble.imp.OnDevConnectListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:43
 * Package name : com.e.ble.core
 * Des :
 */
class ConnectBean {
    private String address;
    private OnDevConnectListener connListener;
    private BluetoothGatt gatt;

    public ConnectBean(String address, OnDevConnectListener connListener) {
        this.address = address;
        this.connListener = connListener;
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

    public OnDevConnectListener getConnListener() {
        return connListener;
    }

    @Override
    public String toString() {
        return "ConnectBean{" +
                "address='" + address + '\'' +
                ", connListener=" + connListener +
                ", gatt=" + gatt +
                '}';
    }
}
