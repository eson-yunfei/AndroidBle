package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothGatt;
import android.content.Context;

import org.eson.ble_sdk.util.BLELog;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public class BLEControl extends BLEBaseControl {

	private BLEControl() {

	}

	private static BLEControl bleControl = null;


	public static void init() {
		if (bleControl == null) {
			bleControl = new BLEControl();
		}

		BLELog.i("BLEControl init ok");
	}

	public static BLEControl get() {
		init();
		return bleControl;
	}


	/**
	 * 连接设备
	 *
	 * @param context
	 * @param deviceMac
	 * @param isAutoConnect
	 */
	public void connectToDevice(Context context, String deviceMac, boolean isAutoConnect,
								BLEConnectCallBack connectCallBack) {

		BLEConnection.get().connectionDevice(context, deviceMac, isAutoConnect, connectCallBack);
	}


	/**
	 * 断开蓝牙连接
	 */
	public void disConnect() {

		BLEConnection.get().disConnection();
	}

	/**
	 * 发送数据
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 * @param data
	 */
	public void sendData(UUID serviceUuid, UUID characteristicUuid,
						 byte[] data, BLEDataTransCallBack bleDataTransCallBack) {

		BLEDataTransport.get().sendData(serviceUuid, characteristicUuid, data, bleDataTransCallBack);

	}


	/**
	 * 激活通知服务
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 * @param descriptorUuid
	 */
	public void enableNotify(UUID serviceUuid, UUID characteristicUuid,
							 UUID descriptorUuid, BLEDataTransCallBack bleDataTransCallBack) {

		BLEDataTransport.get().enableNotify(serviceUuid, characteristicUuid, descriptorUuid, bleDataTransCallBack);


	}

	public void disableNotify(UUID serviceUuid, UUID characteristicUuid, UUID descriptorUuid, BLEDataTransCallBack bleDataTransCallBack) {
		BLEDataTransport.get().disableNotify(serviceUuid, characteristicUuid, descriptorUuid, bleDataTransCallBack);
	}

	public BluetoothGatt getBluetoothGatt() {
		return BLEConnection.get().getBlueToothGatt();
	}


}
