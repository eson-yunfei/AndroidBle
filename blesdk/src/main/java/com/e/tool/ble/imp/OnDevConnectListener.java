package com.e.tool.ble.imp;

import com.e.tool.ble.bean.state.ConnectError;
import com.e.tool.ble.bean.state.ConnectResult;
import com.e.tool.ble.bean.state.DevState;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 15:44
 * Package name : com.e.tool.ble.imp
 * Des :
 */
public interface OnDevConnectListener {
    void onConnectError(ConnectError connectError);

    void onConnectSate(DevState devState);

    void onServicesDiscovered(ConnectResult connectResult);
}
