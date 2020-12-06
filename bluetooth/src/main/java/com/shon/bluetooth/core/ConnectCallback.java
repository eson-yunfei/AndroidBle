package com.shon.bluetooth.core;

import android.bluetooth.BluetoothGatt;

import com.shon.bluetooth.core.callback.OnTimeout;

/**
 * Auth : xiao.yunfei
 * Date : 2020/10/06 22:05
 * Package name : com.shon.bluetooth.core
 * Des :
 */
public abstract class ConnectCallback implements OnTimeout {

    @Override
    public void onTimeout() {

    }

    public void onReTryConnect(int times) {
    }

    public abstract void onConnectSuccess(String address, BluetoothGatt gatt);

    public abstract void onConnectError(String address, int errorCode);

    public abstract void onServiceEnable(String address, BluetoothGatt gatt);

    public  void onConnected(){}

    public abstract void onDisconnected(String address);
}
