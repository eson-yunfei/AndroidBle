package com.e.tool.ble.imp;

import com.e.tool.ble.bean.ConnectBt;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 15:44
 * Package name : com.e.tool.ble.imp
 * Des :
 */
public interface OnDevConnectListener {
   void onConnectSate(int status, int newState);

    void onServicesDiscovered(ConnectBt connectBt);
}
