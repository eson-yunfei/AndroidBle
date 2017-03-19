package com.e.ble.control;

import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.e.ble.bean.BLEUuid;
import com.e.ble.control.listener.BLEConnectionListener;
import com.e.ble.control.listener.BLEReadRssiListener;
import com.e.ble.control.listener.BLEStateChangeListener;
import com.e.ble.control.listener.BLETransportListener;
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

		BLEConnection.get().connectToAddress(context, device, gattCallback);
	}

	/**
	 * 断开所以的设备连接
	 * 多连时可用
	 */
	public void disconnectAll() {
		BLEConnection.get().disConnectAll();
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

		gatt.readRemoteRssi();


	}

	@Override
	public void onReadRssi(String address, int rssi) {

		BLELog.d("BLEControl-->> onReadRssi()");

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
