package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;

import org.eson.ble_sdk.BLESdk;
import org.eson.ble_sdk.control.listener.BLEConnectionListener;
import org.eson.ble_sdk.control.listener.BLEStateChangeListener;
import org.eson.ble_sdk.util.BLELog;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： 设备连接
 */
class BLEConnection implements BLEConnectionListener, BLEStateChangeListener {

	private BluetoothAdapter bluetoothAdapter = null;
	private static BLEConnection bleConnection = null;

	//单连设备
	private BluetoothGatt bluetoothGatt = null;
	private String lastConnectMac = "";

	//多连设备
	private List<BluetoothGatt> gattList;
	private List<String> deviceList;

	//回调接口
	private BLEConnectionListener bleConnectionListener = null;
	private BLEStateChangeListener stateChangeListener = null;

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

	private BLEConnection() {
		bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
	}

	public static BLEConnection get() {
		if (bleConnection == null) {
			bleConnection = new BLEConnection();
		}
		return bleConnection;
	}


	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEConnection 提供给外部的方法，设备连接相关
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|

	/**
	 * 连接设备，单设备连接
	 *
	 * @param deviceAddress
	 *
	 * @return
	 */
	public BluetoothGatt connectToDevice(Context context, String deviceAddress,
										 BluetoothGattCallback gattCallback) {

		if (TextUtils.isEmpty(lastConnectMac)) {
			//之前未连接过设备
			lastConnectMac = deviceAddress;
		} else {
			//之前未连接过设备,
			//判断当前需要连接设备是否与之前连接的设备为同一个设备

			if (deviceAddress.equals(lastConnectMac)) {
				//为同一个设备时，
				// 判断设备是否正为连接状态
				if (isConnect(lastConnectMac)) {
					//设备为已连接的状态，
					//回调正在连接，用户不需要做其他的事情
					onConnected(lastConnectMac);
					return bluetoothGatt;
				} else {
					//设备为未连接的状态
					//设备重连
					if (bluetoothGatt != null) {
						bluetoothGatt.connect();
						return bluetoothGatt;
					}
				}

			} else {
				//不同设备，
				// 判断之前设备是否正为连接状态
				if (isConnect(lastConnectMac)) {
					//设备为已连接的状态，
					//断开之前的设备连接
					bluetoothGatt.disconnect();

				}
				bluetoothGatt.close();
				bluetoothGatt = null;
				//把需要连接的设备赋值给 lastConnectMac
				lastConnectMac = deviceAddress;
			}
		}
		//建立新的设备连接
		BLELog.e("create new bluetoothGatt");
		BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(lastConnectMac);
		bluetoothGatt = bluetoothDevice.connectGatt(context, false, gattCallback);
		return bluetoothGatt;
	}

	/**
	 * 连接设备， 多设备连接
	 *
	 * @param deviceAddress
	 */
	public List<BluetoothGatt> connectToMoreDevice(Context context, String deviceAddress,
												   BluetoothGattCallback gattCallback) {

		//初始化连接列表
		if (gattList == null) {
			gattList = new ArrayList<>();
			deviceList = new ArrayList<>();
		}

		//先判断需要连接的设备是否已存在列表中
		if (isExist(deviceAddress)) {
			//如果已连接过此设备，获取连接过的 BluetoothGatt
			BluetoothGatt gatt = getGattByAddress(deviceAddress);

			if (gatt != null) {
				//如果gatt 为null,重新建立连接
			} else {
				//如果gatt 不为null,判断当前的连接状态
				if (!isConnect(gatt, deviceAddress)) {
					//已断开，重连
					gatt.connect();
				} else {
					//是连接中，返回已连接的状态
					onConnected(deviceAddress);
				}
				return gattList;
			}

		}


		BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
		BluetoothGatt gatt = bluetoothDevice.connectGatt(context, false, gattCallback);
		gattList.add(gatt);
		////未连接过设备，添加新的连接
		deviceList.add(deviceAddress);

		return gattList;
	}


	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEConnection 提供给外部的方法，设备断开连接相关
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|


	/**
	 * 断开设备连接
	 * 单连设备时可用
	 */
	public void disConnect() {

		gattDisconnect(bluetoothGatt);
		bluetoothGatt = null;
	}

	/**
	 * 断开设备连接
	 * 连设备时可用
	 *
	 * @param deviceAddress
	 */
	public void disConnect(String deviceAddress) {

		BluetoothGatt gatt = getGattByAddress(deviceAddress);
		if (gatt == null) {
			return;
		}
		gattDisconnect(gatt);
		gattList.remove(gatt);
		deviceList.remove(deviceAddress);
	}

	/**
	 * 断开所以设备连接
	 * 多连设备时可用
	 */
	public void disConnectAll() {

		if (deviceList == null) {
			return;
		}
		for (String s : deviceList) {
			disConnect(s);
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
	//|  BLEConnection 提供给外部的方法，判断设备连接状态
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|

	/**
	 * 获取当前设备是否为正在连接或已连接状态
	 *
	 * @param deviceAddress
	 *
	 * @return
	 */
	public boolean isConnect(String deviceAddress) {
		return isConnect(bluetoothGatt, deviceAddress);

	}

	/**
	 * 根据设备地址获取 BluetoothGatt 连接
	 *
	 * @param deviceAddress
	 *
	 * @return
	 */
	public BluetoothGatt getGattByAddress(String deviceAddress) {
		BluetoothGatt bluetoothGatt = null;

		if (gattList == null) {
			return null;
		}
		for (BluetoothGatt gatt : gattList) {
			String mac = gatt.getDevice().getAddress();
			if (deviceAddress.equals(mac)) {

				bluetoothGatt = gatt;
				break;
			}
		}

		return bluetoothGatt;
	}

	public boolean isConnect(BluetoothGatt gatt, String deviceAddress) {

		boolean isConnect = false;

		//bluetoothGatt 为null
		if (gatt == null) {
			return isConnect;
		}
		BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

		int state = BLESdk.get().getBluetoothManager().getConnectionState(bluetoothDevice, BluetoothProfile.GATT);
		if (state == BluetoothGatt.STATE_CONNECTED) {
			isConnect = true;
		}

		return isConnect;
	}

	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEConnection 提供给外部的方法，设置回调接口
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|

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
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEConnection 的内部方法，不提供外部访问
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|

	/**
	 * 判断此设备是否在连接列表里面
	 *
	 * @param device
	 *
	 * @return
	 */
	private boolean isExist(String device) {
		boolean isExist = false;
		if (deviceList.size() == 0) {
			return isExist;
		}

		if (deviceList == null) {
			return false;
		}
		for (String s : deviceList) {
			if (s.equals(device)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}


	private void gattDisconnect(BluetoothGatt gatt) {
		if (gatt == null) {
			return;
		}
		gatt.disconnect();
		gatt.close();
	}

	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEConnectionListener  的接口实现
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|

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

	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|  BLEStateChangeListener  的接口实现
	//|                                                                      							|
	//|                                                                      							|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	//|                                                                      							|
	//|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|


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
