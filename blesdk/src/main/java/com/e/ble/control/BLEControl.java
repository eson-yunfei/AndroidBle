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

import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.e.ble.bean.BLEUuid;
import com.e.ble.control.listener.BLEConnectionListener;
import com.e.ble.control.listener.BLEReadRssiListener;
import com.e.ble.control.listener.BLEStateChangeListener;
import com.e.ble.control.listener.BLETransportListener;
import com.e.ble.receiver.BLEStateReceiver;
import com.e.ble.receiver.listener.BLEReceiverListener;
import com.e.ble.util.BLELog;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： 蓝牙控制类，包括设备的连接、断开、数据发送，回调设置
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */

public class BLEControl extends BaseControl {

	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | Sdk  单例模式 初始化
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */
	private static BLEControl bleControl = null;
	private BLEReadRssiListener bleReadRssiListener;

	private BLEReceiverListener mBLEReceiverListener;

	private BLEControl() {
	}

	public static void init() {
		if (bleControl == null) {
			bleControl = new BLEControl();
		}
	}


	public static BLEControl get() {
		if (bleControl == null) {
			init();
		}
		return bleControl;
	}

	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEControl 提供给外部访问的 API   设备连接相关
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 判断一个设备是否为连接状态
	 *
	 * @param deviceAddress
	 *
	 * @return
	 */
	public boolean isConnect(String deviceAddress) {

		return BLEConnection.get().isConnect(deviceAddress);
	}

	/**
	 * 连接到指定的设备
	 *
	 * @param context
	 * @param device
	 */
	public void connectDevice(Context context, String device) {

		BLEConnection.get().connectToAddress(context, device,mBLEGattCallBack);
	}

	/**
	 * 断开所以的设备连接
	 * 多连时可用
	 */
	public void disconnectAll() {
		BLEConnection.get().disConnectAll();
	}

	public void cleanGatt() {
		BLEConnection.get().cleanGatt();
	}

	/**
	 * 断开指定设备的连接
	 *
	 * @param deviceAddress
	 */
	public void disconnect(String deviceAddress) {
		BLEConnection.get().disConnect(deviceAddress);
	}


	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEControl 提供给外部访问的 API   设备连接相关
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */


	/**
	 * Just for demo test
	 * 获取 BluetoothGatt
	 *
	 * @param deviceAddress
	 *
	 * @return
	 */
	public BluetoothGatt getBluetoothGatt(String deviceAddress) {
		return BLEConnectList.get().getGatt(deviceAddress);
	}

	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |  BLEControl 提供给外部访问的 API   设备数据传输相关
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 发送数据
	 *
	 * @param bleUuid
	 */
	public void sendData(BLEUuid bleUuid) {

		BLETransport.get().sendDataToDevice(bleUuid);
	}

	/**
	 * 启用通知
	 *
	 * @param bleUuid
	 */
	public void enableNotify(BLEUuid bleUuid) {
		BLETransport.get().enableNotify(bleUuid);
	}

	/**
	 * 读取数据
	 *
	 * @param bleUuid
	 */
	public void readDeviceData(BLEUuid bleUuid) {
		BLETransport.get().readDeviceData(bleUuid);
	}


	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |  BLEControl 提供给外部访问的 API   读取设备的 Rssi 信号值
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 读取设备的 Rssi
	 *
	 * @param deviceAddress
	 */
	public void readGattRssi(String deviceAddress) {
		BluetoothGatt gatt = BLEConnectList.get().getGatt(deviceAddress);

		if (gatt == null) {
			return;
		}
		gatt.readRemoteRssi();


	}

	@Override
	public void onReadRssi(String address, int rssi) {

//		BLELog.d("BLEControl-->> onReadRssi()");

		if (bleReadRssiListener == null) {
			return;
		}
		bleReadRssiListener.onReadRssi(address, rssi);
	}

	@Override
	public void onReadRssiError(String address, int errorCode) {
		if (bleReadRssiListener == null) {
			return;
		}
		bleReadRssiListener.onReadRssiError(address, errorCode);
	}


	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |  BLEControl 提供给外部访问的 API   设备回调相关
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 设备发送数据，接收数据的回调
	 *
	 * @param bleTransportListener
	 */
	public void setBleTransportListener(BLETransportListener bleTransportListener) {
		BLETransport.get().setBleTransportListener(bleTransportListener);
	}

	/**
	 * 设备连接的回调
	 *
	 * @param connectListener
	 */
	public void setBleConnectListener(BLEConnectionListener connectListener) {
		BLEConnection.get().setBleConnectListener(connectListener);
	}

	/**
	 * 设备状态回调
	 *
	 * @param stateChangedListener
	 */
	public void setBleStateChangedListener(BLEStateChangeListener stateChangedListener) {

		BLEConnection.get().setBleStateChangedListener(stateChangedListener);
	}

	/**
	 * @param readRssiListener
	 */
	public void setBleReadRssiListener(BLEReadRssiListener readRssiListener) {
		bleReadRssiListener = readRssiListener;
	}

	public void setBLEReceiverListener(BLEReceiverListener bleReceiverListener) {

		mBLEReceiverListener = bleReceiverListener;
		if (mBLEReceiverListener == null) {
			mBLEReceiverListener = getBLEReceiverListener();
		}
		BLEStateReceiver.setBLEReceiverListener(mBLEReceiverListener);
	}

	private BLEReceiverListener getBLEReceiverListener() {
		return new BLEReceiverListener() {
			@Override
			public void onStateOff() {
				disconnectAll();
			}

			@Override
			public void onStateOn() {

			}
		};
	}


	/**
	 * |------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |  BLEControl 提供给外部访问的 API   设备回调相关
	 * <p>
	 * |-----------------------------------------------------------------------------------------------------|
	 */

//	@Override
//	protected void closeGatt() {
//		BLEConnection.get().disConnectAll();
//	}
}
