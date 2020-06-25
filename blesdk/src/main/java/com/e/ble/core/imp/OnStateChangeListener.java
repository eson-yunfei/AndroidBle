package com.e.ble.core.imp;

import androidx.annotation.NonNull;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:06
 * Package name : com.e.ble.core.imp
 * Des : 设备连接状态监听
 */
public interface OnStateChangeListener {

    /**
     * 设备连接中
     *
     * @param address mac
     */
    void onConnecting(@NonNull String address);

    /**
     * 设备已连接
     *
     * @param address mac
     */
    void onConnected(@NonNull String address);

    /**
     * 设备正在断开连接
     *
     * @param address mac
     */
    void onDisconnecting(@NonNull String address);

    /**
     * 设备已断开
     *
     * @param address mac
     */
    void onDisconnected(@NonNull String address);
}
