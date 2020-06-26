package org.eson.liteble.activity.base;

import com.e.tool.ble.annotation.LinkState;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 16:42
 * Package name : org.eson.liteble.activity.base
 * Des :
 */
public interface IObserve {

    void observerViewModel();

    void onDeviceStateChange(String deviceMac,@LinkState int currentState);
}
