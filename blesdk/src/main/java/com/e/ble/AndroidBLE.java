package com.e.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.e.ble.util.BLELog;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： AndroidBLE  SDK 内部访问的 类
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */
class AndroidBLE {
	private static AndroidBLE androidBLE = null;
	private Context context;
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;

	// |---------------------------------------------------------------------------------------------------------------|
	//初始化操作，不说了
	private AndroidBLE(Context context) {
		this.context = context;
	}

	public static void init(Context context) {

		if (androidBLE == null) {
			androidBLE = new AndroidBLE(context);
		}
		BLELog.i("AndroidBLE init ok");
	}

	public static AndroidBLE get() throws NullPointerException {
		if (androidBLE == null) {
			throw new NullPointerException("AndroidBLE not init");
		}
		return androidBLE;
	}
// |---------------------------------------------------------------------------------------------------------------------|

	/**
	 * 获取系统 BluetoothManager
	 *
	 * @return
	 */
	public BluetoothManager getBluetoothManager() {
		if (bluetoothManager == null) {
			bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		}
		return bluetoothManager;
	}

	/**
	 * 获取系统 BluetoothAdapter
	 *
	 * @return
	 */
	public BluetoothAdapter getBluetoothAdapter() throws NullPointerException {
		getBluetoothManager();
		if (bluetoothManager == null) {

			BLELog.e("AndroidBLE.java------->>>bluetoothManager is null");
			throw new NullPointerException("AndroidBLE.java : getBluetoothAdapter() : bluetoothManager is null");
		}
		if (bluetoothAdapter == null) {
			bluetoothAdapter = bluetoothManager.getAdapter();
		}
		return bluetoothAdapter;


	}

	/**
	 * 重置 bluetoothManager ， bluetoothAdapter
	 */
	public void reset() {
//		bluetoothAdapter = null;
//		bluetoothManager = null;

	}
}
