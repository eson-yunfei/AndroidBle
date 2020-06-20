package org.eson.liteble.activity.base;

import com.e.ble.annotation.BLESate;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 16:42
 * Package name : org.eson.liteble.activity.base
 * Des :
 */
public interface IObserve {

    void observerViewModel();

    void onDeviceStateChange(String deviceMac,@BLESate int currentState);
}
