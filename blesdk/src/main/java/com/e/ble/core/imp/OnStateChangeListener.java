package com.e.ble.core.imp;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:06
 * Package name : com.e.ble.core.imp
 * Des :
 */
public interface OnStateChangeListener {
    void onConnecting(String address);

    void onConnected(String address);

    void onDisconnecting(String address);

    void onDisconnected(String address);
}
