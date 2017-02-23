package org.eson.ble_sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
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
			return null;
		}
		if (bluetoothAdapter == null) {
			bluetoothAdapter = bluetoothManager.getAdapter();
		}
		return bluetoothAdapter;
	}
}
