package com.e.tool.ble.gatt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import com.e.tool.ble.gatt.imp.ReadRssiListener;
import com.e.tool.ble.imp.OnReadRssiCallBack;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 16:35
 * Package name : com.e.tool.ble.gatt
 * Des :
 */
class ReadRssiImpl extends BaseImpl implements ReadRssiListener {
    private OnReadRssiCallBack onReadRssiCallBack;

    ReadRssiImpl() {
        super();
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        if (onReadRssiCallBack == null || status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
        BluetoothDevice device = gatt.getDevice();
        if (device == null) {
            return;
        }
        String address = device.getAddress();
        String name = device.getName();
        post(() -> onReadRssiCallBack.onReadRssi(address, name, rssi));

    }

    @Override
    public void setOnReadRssiCallBack(OnReadRssiCallBack onReadRssiCallBack) {
        this.onReadRssiCallBack = onReadRssiCallBack;
    }
}
