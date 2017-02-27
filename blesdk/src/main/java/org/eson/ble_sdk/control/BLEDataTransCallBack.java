package org.eson.ble_sdk.control;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public interface BLEDataTransCallBack {

	void onCharRead(String uuid, byte[] data);

	void onCharWrite(String uuid, byte[] data);

	void onNotify(String uuid, byte[] data);
}
