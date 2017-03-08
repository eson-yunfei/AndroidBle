package org.eson.ble_sdk.control.listener;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/7
 * @说明： 蓝牙连接状态的回调
 */

public interface BLEStateChangeListener {

	void onStateConnected(String address);

	void onStateConnecting(String address);

	void onStateDisConnecting(String address);

	void onStateDisConnected(String address);
}
