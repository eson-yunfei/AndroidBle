package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.text.TextUtils;

import org.eson.ble_sdk.BLESdk;
import org.eson.ble_sdk.util.BLELog;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/23
 * @说明：
 */

class BLEConnection extends BLEBaseControl {

	private BLEConnection() {

	}

	private static BLEConnection bleConnection = null;

	public static BLEConnection get() {
		if (bleConnection == null) {
			bleConnection = new BLEConnection();
		}
		return bleConnection;
	}


	/**
	 * @param context
	 * @param deviceMac
	 * @param isAutoConnect
	 * @param connectCallBack
	 */
	public void connectionDevice(Context context, String deviceMac,
								 boolean isAutoConnect, BLEConnectCallBack connectCallBack) {

		if (TextUtils.isEmpty(deviceMac)) {
			return;
		}

		if (connectCallBack != null) {
			connectCallBackList.add(connectCallBack);
		}

		if (bluetoothGatt != null) {
			bluetoothGatt.connect();
			return;
		}

		if (bluetoothAdapter == null) {
			bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
		}

		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceMac);
		if (device == null) {
			return;
		}

		bluetoothGatt = device.connectGatt(context, isAutoConnect, gattCallback);
		BLELog.e("connectionDevice"+bluetoothGatt.getServices().size());
		bluetoothGatt.connect();

	}

	public BluetoothGatt getBlueToothGatt(){
		return bluetoothGatt;
	}

	/**
	 *
	 */
	public void disConnection() {
		if (bluetoothGatt == null) {
			return;
		}
		bluetoothGatt.disconnect();
		bluetoothGatt = null;
	}


	@Override
	public void onConnecting() {
		super.onConnecting();

		if (connectCallBackList.size() == 0) {
			return;
		}

		for (BLEConnectCallBack connectCallBack : connectCallBackList) {
			connectCallBack.onConnecting();
		}
	}

	@Override
	public void onConnected() {
		super.onConnected();
		if (connectCallBackList.size() == 0) {
			return;
		}

		for (BLEConnectCallBack connectCallBack : connectCallBackList) {
			connectCallBack.onConnected();
		}
	}

	@Override
	public void onDisConnecting() {
		super.onDisConnecting();
		if (connectCallBackList.size() == 0) {
			return;
		}

		for (BLEConnectCallBack connectCallBack : connectCallBackList) {
			connectCallBack.onDisConnecting();
		}
	}

	@Override
	public void onDisConnected() {
		super.onDisConnected();
		if (connectCallBackList.size() == 0) {
			return;
		}

		for (BLEConnectCallBack connectCallBack : connectCallBackList) {
			connectCallBack.onDisConnected();
		}
	}


	@Override
	public void removeConnectCallBack(BLEConnectCallBack connectCallBack) {
		super.removeConnectCallBack(connectCallBack);


		if (connectCallBack == null || connectCallBackList.size() == 0) {
			return;
		}

		for (int i = 0; i < connectCallBackList.size(); i++) {

			if (connectCallBackList.contains(connectCallBack)) {
				connectCallBackList.remove(connectCallBack);
			}
		}
	}

	@Override
	public void cleanConnectCallBack() {
		super.cleanConnectCallBack();
		if (connectCallBackList.size() == 0) {
			return;
		}
		connectCallBackList.clear();
	}
}
