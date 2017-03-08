package org.eson.ble_sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import org.eson.ble_sdk.check.BLECheck;
import org.eson.ble_sdk.control.BLEControl;
import org.eson.ble_sdk.scan.BLEScanner;
import org.eson.ble_sdk.util.BLELog;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 蓝牙工具类的总入口
 */

public class BLESdk {

	/**
	 * |----------------------------------------------------------------------|
	 * |                                                                      |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |
	 * |<p>  初始化变量、对象
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |																	  |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * /**
	 * 是否允许连接多个设备
	 */
	private boolean permitConnectMore = false;

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
	 */
	public static void init(Context context) {

		AndroidBLE.init(context);                //初始化 BluetoothManager,BluetoothAdapter.
		BLECheck.init();                    //初始化蓝牙检测类
		BLEScanner.init();                    //初始化蓝牙扫描类
		BLEControl.init();//	初始化蓝牙控制类

		BLELog.i("BLESdk init ok");
	}

	/**
	 * |----------------------------------------------------------------------|
	 * |                                                                      |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |
	 * |<p>  提供给外部的API
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |																	  |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 */
	public boolean isPermitConnectMore() {
		return permitConnectMore;
	}

	/**
	 * 是否开启多连设备
	 *
	 * @param permitConnectMore
	 */
	public void setPermitConnectMore(boolean permitConnectMore) {
		this.permitConnectMore = permitConnectMore;
	}


	public BluetoothManager getBluetoothManager() {
		return AndroidBLE.get().getBluetoothManager();
	}

	public BluetoothAdapter getBluetoothAdapter() {
		return AndroidBLE.get().getBluetoothAdapter();
	}

}
