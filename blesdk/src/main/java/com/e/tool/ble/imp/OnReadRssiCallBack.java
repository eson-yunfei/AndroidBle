package com.e.tool.ble.imp;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 16:29
 * Package name : com.e.tool.ble.imp
 * Des :
 */
public interface OnReadRssiCallBack {
    void onReadRssi(String address, String name, int rssi);
}
