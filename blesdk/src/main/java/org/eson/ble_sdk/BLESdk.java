package org.eson.ble_sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import org.eson.ble_sdk.check.BLECheck;
import org.eson.ble_sdk.control.BLEControl;
import org.eson.ble_sdk.scan.BLEScanner;
import org.eson.ble_sdk.util.BLELog;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 蓝牙工具类的总入口
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

	/**
	 * 初始化SDK
	 *
	 * @param context
	 * @param service
	 * @param desc
	 * @param write
	 * @param notify
	 */
	public static void init(Context context, UUID service, UUID desc, UUID[] write, UUID[] notify) {

		initUUID(service, desc, write, notify);

		AndroidBLE.init(context);                //初始化 BluetoothManager,BluetoothAdapter.
		BLECheck.init();                    //初始化蓝牙检测类
		BLEScanner.init();                    //初始化蓝牙扫描类
		BLEControl.init();                    //	初始化蓝牙控制类

		BLELog.i("BLESdk init ok");
	}

	private static void initUUID(UUID service, UUID desc, UUID[] write, UUID[] notify) {
		BLE_UUID.UUID_SERVICE = service;
		BLE_UUID.UUID_DESC = desc;
		BLE_UUID.UUID_WRITE = write;
		BLE_UUID.UUID_NOTIFY = notify;
	}

	public BluetoothManager getBluetoothManager() {
		return AndroidBLE.get().getBluetoothManager();
	}

	public BluetoothAdapter getBluetoothAdapter() {
		return AndroidBLE.get().getBluetoothAdapter();
	}

}
