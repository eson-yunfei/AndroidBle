package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothGatt;
import android.content.Context;

import org.eson.ble_sdk.BLESdk;
import org.eson.ble_sdk.bean.BLEUuid;
import org.eson.ble_sdk.control.listener.BLEConnectionListener;
import org.eson.ble_sdk.control.listener.BLEReadRssiListener;
import org.eson.ble_sdk.control.listener.BLEStateChangeListener;
import org.eson.ble_sdk.control.listener.BLETransportListener;
import org.eson.ble_sdk.util.BLELog;


/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： 蓝牙控制类，包括设备的连接、断开、数据发送，回调设置
 */

public class BLEControl extends BaseControl {


	private static BLEControl bleControl = null;
	private BluetoothGatt bluetoothGatt;

	private BLEReadRssiListener bleReadRssiListener;

	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEConnection 单例实现
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
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

	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEControl 提供给外部访问的 API   设备连接相关
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|

	/**
	 * 设备连接
	 *
	 * @param context
	 * @param device
	 */
	public void connectDevice(Context context, String device) {
		if (BLESdk.get().isPermitConnectMore()) {
			//多连设备
			BLEConnection.get().connectToMoreDevice(context, device, gattCallback);
		} else {
			//
			bluetoothGatt = BLEConnection.get().connectToDevice(context, device, gattCallback);
		}
	}

	/**
	 * 断开设备连接
	 */
	public void disconnect() {

		BLEConnection.get().disConnect();
	}

	/**
	 * 断开所以设备连接
	 * 多连时可用
	 */
	public void disconnectAll() {
		BLEConnection.get().disConnectAll();
	}

	/**
	 * 断开指定设备连接
	 * 多连时可用
	 *
	 * @param deviceAddress
	 */
	public void disconnect(String deviceAddress) {
		BLEConnection.get().disConnect(deviceAddress);
	}


	/**
	 * 判断一个设备是否为连接状态
	 *
	 * @param deviceAddress
	 *
	 * @return
	 */
	public boolean isConnect(String deviceAddress) {
		if (BLESdk.get().isPermitConnectMore()) {
			BluetoothGatt gatt = BLEConnection.get().getGattByAddress(deviceAddress);
			if (gatt == null) {
				return false;
			} else {
				return BLEConnection.get().isConnect(gatt, deviceAddress);
			}
		} else {
			return BLEConnection.get().isConnect(deviceAddress);
		}
	}

	/**
	 * 获取 BluetoothGatt
	 * @param deviceAddress
	 * @return
	 */
	public BluetoothGatt getBluetoothGatt(String deviceAddress) {
		if (BLESdk.get().isPermitConnectMore()) {
			BluetoothGatt gatt = BLEConnection.get().getGattByAddress(deviceAddress);
			return gatt;
		} else {
			return bluetoothGatt;
		}
	}


	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEControl 提供给外部访问的 API   设备数据传输相关
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|


	/**
	 * 发送数据
	 *
	 * @param bleUuid
	 */
	public void sendData(BLEUuid bleUuid) {

		if (BLESdk.get().isPermitConnectMore()) {
			//多连设备

			BluetoothGatt gatt = BLEConnection.get().getGattByAddress(bleUuid.getAddress());
			BLETransport.get().sendDataToDevice(gatt, bleUuid);
		} else {
			//
			BLETransport.get().sendDataToDevice(bluetoothGatt, bleUuid);
		}
	}

	/**
	 * 启用通知
	 *
	 * @param bleUuid
	 */
	public void enableNotify(BLEUuid bleUuid) {
		if (BLESdk.get().isPermitConnectMore()) {
			//多连设备

			BluetoothGatt gatt = BLEConnection.get().getGattByAddress(bleUuid.getAddress());
			BLETransport.get().enableNotify(gatt, bleUuid);
		} else {
			//
			BLETransport.get().enableNotify(bluetoothGatt, bleUuid);
		}
	}

	/**
	 * 读取数据
	 *
	 * @param bleUuid
	 */
	public void readDeviceData(BLEUuid bleUuid) {
		if (BLESdk.get().isPermitConnectMore()) {
			//多连设备

			BluetoothGatt gatt = BLEConnection.get().getGattByAddress(bleUuid.getAddress());
			BLETransport.get().readDeviceData(gatt, bleUuid);
		} else {
			//
			BLETransport.get().readDeviceData(bluetoothGatt, bleUuid);
		}
	}

	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEControl 提供给外部访问的 API   读取设备的 Rssi 信号值
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|


	/**
	 * 读取设备的 Rssi
	 *
	 * @param deviceAddress
	 */
	public void readGattRssi(String deviceAddress) {
		if (BLESdk.get().isPermitConnectMore()) {
			//多连设备
			BluetoothGatt gatt = BLEConnection.get().getGattByAddress(deviceAddress);
			if (!BLEConnection.get().isConnect(gatt, deviceAddress)) {
				return;
			}
			gatt.readRemoteRssi();
		} else {
			//
			if (!BLEConnection.get().isConnect(bluetoothGatt, deviceAddress)) {
				return;
			}
			bluetoothGatt.readRemoteRssi();
		}
	}

	@Override
	public void onReadRssi(String address, int rssi) {
		super.onReadRssi(address, rssi);

		BLELog.e("BLEControl-->> onReadRssi()");

		if (bleReadRssiListener == null) {
			return;
		}
		bleReadRssiListener.onReadRssi(address, rssi);
	}


	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEControl 提供给外部访问的 API   设备回调相关
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|


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


}
