package org.eson.ble_sdk.control.listener;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： 设备连接状态接口
 * <p>
 * 包括设备的连接
 */

public interface BLEConnectionListener {

	/**
	 * 连接设备异常
	 *
	 * @param address
	 * @param errorCode
	 */
	void onConnectError(String address, int errorCode);

	/**
	 * 设备连接成功
	 *
	 * @param address
	 */
	void onConnectSuccess(String address);

	/**
	 * 设备已经连接
	 *
	 * @param address
	 */
	void onConnected(String address);

}
