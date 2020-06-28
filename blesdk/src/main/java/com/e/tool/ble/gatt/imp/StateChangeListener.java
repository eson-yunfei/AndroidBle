package com.e.tool.ble.gatt.imp;

import android.bluetooth.BluetoothGatt;

import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.tool.ble.imp.OnStateChanged;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 18:56
 * Package name : com.e.tool.ble.gatt.imp
 * Des :
 */
public interface StateChangeListener {
    void onConnectionStateChange(BluetoothGatt gatt, int status, int newState);

    void onServicesDiscovered(BluetoothGatt gatt, int status);

    void setOnStateChangeListener(OnStateChanged onStateChangeListener);

    void setConnectCallBack(OnDevConnectListener connectListener);
}
