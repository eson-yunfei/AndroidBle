package com.shon.bluetooth.core.gatt;

import android.bluetooth.BluetoothGatt;

import com.shon.bluetooth.DataDispatcher;

/**
 * Auth : xiao.yunfei
 * Date : 2020/9/28 16:26
 * Package name : com.shon.bluetooth.core.gatt
 * Des :
 */
public class RssiGattCallback {

    private DataDispatcher dataDispatcher;

    public RssiGattCallback(DataDispatcher dataDispatcher) {
        this.dataDispatcher = dataDispatcher;
    }

    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

    }
}