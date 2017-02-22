package org.eson.ble_sdk.check;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 蓝牙检测工具
 */

public class BLECheck {
	private BLECheck() {

	}

	private static BLECheck bleCheck;

	public static void init() {
		if (bleCheck == null) {
			bleCheck = new BLECheck();
		}
	}

	public static BLECheck get() {
		init();
		return bleCheck;
	}
}
