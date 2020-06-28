package com.e.tool.ble.gatt.imp;

import android.bluetooth.BluetoothGatt;

import com.e.tool.ble.imp.OnReadRssiCallBack;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 16:34
 * Package name : com.e.tool.ble.gatt.imp
 * Des :
 */
public interface ReadRssiListener {
    void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status);

    void setOnReadRssiCallBack(OnReadRssiCallBack onReadRssiCallBack);
}
