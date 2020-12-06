package com.shon.bluetooth.core;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;

import com.shon.bluetooth.BLEManager;

/**
 * Auth : xiao.yunfei
 * Date : 2020/10/06 21:24
 * Package name : com.shon.bluetooth.core
 * Des :
 */
public class Connect {
    private final String address;
    private int reTryTimes = 0;
    private long timeOut = 10_0000;

    private ConnectCallback connectCallback;
    public Connect(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public ConnectCallback getConnectCallback() {
        return connectCallback;
    }

    public Connect setTimeout(long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public Connect setReTryTimes(int reTryTimes) {
        this.reTryTimes = 0;
        return this;
    }


    public int getReTryTimes() {
        return reTryTimes;
    }

    public void enqueue(ConnectCallback connectCallback){
        this.connectCallback = connectCallback;
        BLEManager.getInstance().getConnectDispatcher().enqueue(this);
    }


    public void connect(Context context, BluetoothDevice bluetoothDevice, BluetoothGattCallback callback){

        BluetoothGatt bluetoothGatt = bluetoothDevice.connectGatt(context, false, callback);
    }
}
