package com.e.tool.ble.imp;

import com.e.tool.ble.bean.state.DevState;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:06
 * Package name : com.e.tool.ble.imp
 * Des : 设备连接状态监听
 */
public interface OnStateChanged {


    void onSateChanged(DevState devState);
}
