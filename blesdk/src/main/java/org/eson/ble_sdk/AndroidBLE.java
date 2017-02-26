package org.eson.ble_sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import org.eson.ble_sdk.util.BLELog;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 私有文件，提供BluetoothManager  ，BluetoothAdapter
 */

class AndroidBLE {
	private static AndroidBLE androidBLE = null;
	private Context context;
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;

	private AndroidBLE(Context context) {
		this.context = context;
	}


	public static void init(Context context) {

		if (androidBLE == null) {
			androidBLE = new AndroidBLE(context);
		}
		BLELog.i("AndroidBLE init ok");
	}

	public static AndroidBLE get() {

		return androidBLE;
	}


	public BluetoothManager getBluetoothManager() {
		if (bluetoothManager == null) {
			bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		}
		return bluetoothManager;
	}

	public BluetoothAdapter getBluetoothAdapter() {
		getBluetoothManager();
		if (bluetoothManager == null) {
			BLELog.e("AndroidBLE.java------->>>bluetoothManager is null");
			return null;
		}
		if (bluetoothAdapter == null) {
			bluetoothAdapter = bluetoothManager.getAdapter();
		}
		return bluetoothAdapter;


	}
}
