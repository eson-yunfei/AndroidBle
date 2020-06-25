package com.e.ble.core.imp;

import com.e.ble.core.bean.ConnectBt;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 15:44
 * Package name : com.e.ble.core.imp
 * Des :
 */
public interface OnConnectListener {
   void onConnectSate(int status, int newState);

    void onServicesDiscovered(ConnectBt connectBt);
}
