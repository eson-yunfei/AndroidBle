package org.eson.ble_sdk.control.listener;


import org.eson.ble_sdk.bean.BLECharacter;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明：
 */

public interface BLETransportListener {


	void onCharacterRead(BLECharacter bleCharacter);

	void onCharacterWrite(BLECharacter bleCharacter);

	void onCharacterNotify(BLECharacter bleCharacter);
}
