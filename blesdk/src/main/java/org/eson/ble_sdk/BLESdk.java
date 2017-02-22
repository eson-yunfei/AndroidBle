package org.eson.ble_sdk;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：蓝牙工具类的总入口
 */

public class BLESdk {

	private BLESdk() {

	}

	private static BLESdk instance = null;

	public static BLESdk get() {

		if (instance == null) {
			instance = new BLESdk();
		}
		return instance;
	}
}
