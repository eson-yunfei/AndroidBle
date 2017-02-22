package org.eson.ble_sdk.scan;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public class BLEScanner {

	private BLEScanner() {

	}

	private static BLEScanner bleScanner = null;

	public static void init() {
		if (bleScanner == null) {
			bleScanner = new BLEScanner();
		}
	}

	public static BLEScanner get() {
		init();
		return bleScanner;
	}


}
