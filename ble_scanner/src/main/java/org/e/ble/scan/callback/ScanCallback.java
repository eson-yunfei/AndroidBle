package org.e.ble.scan.callback;

import org.e.ble.scan.support.ScanResult;

import java.util.List;

public abstract class ScanCallback {
	public static final int SCAN_FAILED_ALREADY_STARTED = 1;

	public static final int SCAN_FAILED_APPLICATION_REGISTRATION_FAILED = 2;

	public static final int SCAN_FAILED_INTERNAL_ERROR = 3;

	public static final int SCAN_FAILED_FEATURE_UNSUPPORTED = 4;

	public static final int SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES = 5;

	public void onScanResult(ScanResult result) {
	}

	public void onBatchScanResults(List<ScanResult> results) {
	}

	public void onScanFailed(int errorCode) {
	}
}
