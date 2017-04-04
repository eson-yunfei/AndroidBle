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

package com.e.ble.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.e.ble.BLESdk;
import com.e.ble.check.BLECheck;
import com.e.ble.control.listener.BLEConnectionListener;
import com.e.ble.control.listener.BLEStateChangeListener;
import com.e.ble.util.BLEError;
import com.e.ble.util.BLELog;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： 设备 连接&断开 控制类;  包括设备的状态监听
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */
class BLEConnection implements BLEConnectionListener, BLEStateChangeListener {

	private static BLEConnection bleConnection = null;

	private BluetoothAdapter bluetoothAdapter = null;

	//回调接口
	private BLEConnectionListener bleConnectionListener = null;
	private BLEStateChangeListener stateChangeListener = null;


	private Handler mHandler;

	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEConnection 的实例化相关 ，单例模式，不允许外部使用 new 关键字进行实例化
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	private BLEConnection() {
		bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
		mHandler = new Handler();
	}

	public static BLEConnection get() {
		if (bleConnection == null) {
			bleConnection = new BLEConnection();
		}
		return bleConnection;
	}


	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEConnection 提供给外部的方法，设备连接相关
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 连接到指定设备
	 *  @param context
	 * @param address
	 * @param bleGattCallBack
	 */
	public void connectToAddress(@NonNull final Context context, @NonNull final String address, final BaseControl.BLEGattCallBack bleGattCallBack) {


		if (BLEConnectList.get().outLimit(address)) {
			//超出最多设置的连接数，返回超限，
			onConnectError(address, BLEError.BLE_OUT_MAX_CONNECT);
			return;
		}

		if (!BLECheck.get().isBleEnable()) {
			//蓝牙是否为打开
			onConnectError(address, BLEError.BLE_CLOSE);
			return;
		}
		if (context == null) {
			BLELog.e("BLEConnection :: connectToAddress()  context is null");
			onConnectError(address, 0);
			return;
		}

		if (TextUtils.isEmpty(address)) {
			BLELog.e("BLEConnection :: connectToAddress()  address is null");
			onConnectError(address, 0);
			return;
		}


		mHandler.post(new Runnable() {
			@Override
			public void run() {
				BluetoothGatt gatt = BLEConnectList.get().getGatt(address);
				boolean isGattConnect = getGattState(gatt, address);
				if (isGattConnect) {
					return;
				}

				BLELog.e("create new bluetoothGatt");
				final BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
				if (bluetoothDevice == null) {
					return;
				}
				gatt = bluetoothDevice.connectGatt(context, false, bleGattCallBack);
				BLELog.e("create new bluetoothGatt  finish");
				BLEConnectList.get().putGatt(address, gatt);
			}
		});


	}

	private boolean getGattState(@NonNull BluetoothGatt gatt, @NonNull String address) {

		if (gatt == null) {
			return false;
		}

		BLELog.e("BLEConnection :: connectToAddress() gatt exist");
		if (isConnect(gatt, address)) {
			//如果还正在连接，不进行任何操作
			BLELog.e("BLEConnection :: connectToAddress() gatt connected");
			onConnected(address);
			return true;
		}

		//如果已断开连接
		BLELog.e("BLEConnection :: connectToAddress() gatt disConnected and reConnect");
//		gatt.connect();
		gatt.close();
		return false;
	}

	/**
	 * 断开设备连接
	 * 连设备时可用
	 *
	 * @param deviceAddress
	 */
	public void disConnect(String deviceAddress) {

		BLEConnectList.get().disconnect(deviceAddress);
	}

	/**
	 * 断开所以设备连接
	 * 多连设备时可用
	 */
	public void disConnectAll() {

		BLEConnectList.get().disconnectAll();
	}

	public void cleanGatt() {

		BLEConnectList.get().cleanGatt();
	}


	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEConnection 提供给外部的方法，判断设备连接状态
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 获取当前设备是否为正在连接或已连接状态
	 *
	 * @param deviceAddress
	 *
	 * @return
	 */
	public boolean isConnect(String deviceAddress) {
		BluetoothGatt gatt = BLEConnectList.get().getGatt(deviceAddress);
		return isConnect(gatt, deviceAddress);
	}

	public boolean isConnect(BluetoothGatt gatt, String deviceAddress) {

		if (gatt == null) {
			return false;
		}

		boolean isConnect = false;

		//bluetoothGatt 为null

		BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

		int state = BLESdk.get().getBluetoothManager().getConnectionState(bluetoothDevice, BluetoothProfile.GATT);
		if (state == BluetoothGatt.STATE_CONNECTED) {
			isConnect = true;
		}

		return isConnect;
	}

	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEConnection 提供给外部的方法，设置回调接口
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	/***
	 * 设置设备连接回调
	 * @param connectListener
	 */
	public void setBleConnectListener(BLEConnectionListener connectListener) {
		bleConnectionListener = connectListener;
	}

	/**
	 * 设置设备的状态监听
	 *
	 * @param stateChangedListener
	 */
	public void setBleStateChangedListener(BLEStateChangeListener stateChangedListener) {
		this.stateChangeListener = stateChangedListener;
	}


	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEConnectionListener  的接口实现
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	@Override
	public void onConnected(String address) {
		if (bleConnectionListener != null) {
			bleConnectionListener.onConnected(address);
		}
	}


	@Override
	public void onConnectError(String address, int errorCode) {
		if (bleConnectionListener != null) {
			bleConnectionListener.onConnectError(address, errorCode);
		}
	}

	@Override
	public void onConnectSuccess(String address) {
		if (bleConnectionListener != null) {
			bleConnectionListener.onConnectSuccess(address);
		}
	}

	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEStateChangeListener  的接口实现
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */
	@Override
	public void onStateConnected(String address) {

		if (stateChangeListener != null) {
			stateChangeListener.onStateConnected(address);
		}
	}

	@Override
	public void onStateConnecting(String address) {
		if (stateChangeListener != null) {
			stateChangeListener.onStateConnecting(address);
		}
	}

	@Override
	public void onStateDisConnecting(String address) {
		if (stateChangeListener != null) {
			stateChangeListener.onStateDisConnecting(address);
		}
	}

	@Override
	public void onStateDisConnected(String address) {
		if (stateChangeListener != null) {
			stateChangeListener.onStateDisConnected(address);
		}
	}

}
