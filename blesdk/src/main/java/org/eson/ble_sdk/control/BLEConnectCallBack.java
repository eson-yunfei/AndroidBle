package org.eson.ble_sdk.control;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public interface BLEConnectCallBack {

	void onConnecting();

	void onConnected();

	void onDisConnecting();

	void onDisConnected();

	void onBleServerEnable();
}
