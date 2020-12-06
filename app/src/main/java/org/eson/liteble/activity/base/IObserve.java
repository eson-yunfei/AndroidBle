package org.eson.liteble.activity.base;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 16:42
 * Package name : org.eson.liteble.activity.base
 * Des :
 */
public interface IObserve {

    void observerViewModel();

    void onDeviceStateChange(String deviceMac,int currentState);
}
