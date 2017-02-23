package org.eson.ble_sdk.scan;

import org.eson.ble_sdk.bean.BLEDevice;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public interface BLEScanListener {

	void onScannerStart();

	void onScanning(BLEDevice device);

	void onScannerStop();

	void onScannerError();
}
