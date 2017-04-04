/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.e.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.e.ble.check.BLECheck;
import com.e.ble.control.BLEControl;
import com.e.ble.scan.BLEScanner;
import com.e.ble.util.BLELog;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 * <p>
 * | @作者 xiaoyunfei
 * <p>
 * | @日期: 2017/2/22
 * <p>
 * | @说明： 蓝牙工具类的总入口
 * <p>
 * |--------------------------------------------------------------------------------------------------------------|
 */

public class BLESdk {

	/**
	 * 是否允许多个设备设备同时连接，
	 * <p>
	 * true 为运行多连
	 */
	private boolean permitConnectMore = false;

	//同时离连接的最大个数
	private int maxConnect = 3;

	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |
	 * <p>
	 * | Sdk 的实例化操作，单例模式，不允许外部使用 new 关键字进行实例化
	 * <p>
	 * |
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */
	//私有方法，不允许外部使用 new 关键字
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
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |
	 * <p>
	 * | Sdk 提供给外部的接口
	 * <p>
	 * |
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */


	/**
	 * <p>
	 * /** 初始化与 SDK 相关的一些类
	 * <p>
	 * 此方法必须调用，最好是直接在 Application 里面调用
	 *
	 * @param context
	 */
	public void init(Context context) {

		AndroidBLE.init(context);                //初始化 BluetoothManager,BluetoothAdapter.
		BLECheck.init();                    //初始化蓝牙检测类
		BLEScanner.init();                    //初始化蓝牙扫描类
		BLEControl.init();//	初始化蓝牙控制类

		BLELog.i("BLESdk init ok");
	}


	/**
	 * 设置是否允许多连
	 *
	 * @return
	 */
	public boolean isPermitConnectMore() {
		return permitConnectMore;
	}


	/**
	 * 设置最大连接数
	 *
	 * @param maxConnect
	 */
	public void setMaxConnect(int maxConnect) {

		if (maxConnect <1){
			maxConnect = 1;
		}

		if (maxConnect > 5){
			maxConnect = 5;
		}
		if (maxConnect == 1) {
			permitConnectMore = false;
		} else {
			permitConnectMore = true;
		}

		this.maxConnect = maxConnect;
	}

	/**
	 * 获取最大连接数
	 *
	 * @return
	 */
	public int getMaxConnect() {
		return maxConnect;
	}


	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |
	 * <p>
	 * | Sdk 提供的获取系统的 BluetoothManager，BluetoothAdapter 的方法，有 SDK 内部使用，外部最后不要使用
	 * <p>
	 * |
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 获取 BluetoothManager
	 *
	 * @return
	 */
	public BluetoothManager getBluetoothManager() throws NullPointerException {
		return AndroidBLE.get().getBluetoothManager();
	}

	/**
	 * 获取 BluetoothAdapter
	 *
	 * @return
	 */
	public BluetoothAdapter getBluetoothAdapter() throws NullPointerException {
		return AndroidBLE.get().getBluetoothAdapter();
	}


	public void reset() throws NullPointerException {

		AndroidBLE.get().reset();
	}
}
