package org.eson.ble_sdk.control;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public interface BLEDataTransCallBack {

	void onCharacteristicRead();

	void onCharacteristicWrite();

	void onNotify(byte[] data);
}
