package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;

import com.e.tool.ble.gatt.BGattCallBack;
import com.e.tool.ble.imp.OnReadRssiCallBack;
import com.e.tool.ble.request.Request;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 16:27
 * Package name : com.e.tool.ble.control
 * Des :
 */
class ReadRssiRequest extends Request {
    private String address;
    private OnReadRssiCallBack onReadRssiCallBack;
    private BGattCallBack gattCallBack;

    public ReadRssiRequest(String address, BGattCallBack gattCallBack, OnReadRssiCallBack onReadRssiCallBack) {

        this.address = address;
        this.gattCallBack = gattCallBack;
        this.onReadRssiCallBack = onReadRssiCallBack;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean launch() {

        BluetoothGatt gatt = gattCallBack.getBluetoothGatt(address);
        if (gatt == null) {
            return false;
        }
        return gatt.readRemoteRssi();
    }


    public void onReadRssi(String address, String name, int rssi) {

        if (onReadRssiCallBack != null) {
            onReadRssiCallBack.onReadRssi(address, name, rssi);
        }

    }
}
