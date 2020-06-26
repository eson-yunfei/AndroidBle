package com.e.tool.ble.imp;

import com.e.tool.ble.bean.NotifyMessage;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 19:34
 * Package name : com.e.tool.ble.imp
 * Des :
 */
public interface OnDataNotify {

    void onDataNotify(NotifyMessage notifyMessage);
}
