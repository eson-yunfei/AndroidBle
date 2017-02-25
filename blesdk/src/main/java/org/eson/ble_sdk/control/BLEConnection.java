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

	private BLEConnectCallBack bleConnectCallBack;

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

		if (bluetoothGatt != null) {
			bluetoothGatt.connect();
			return;
		}

		bleConnectCallBack = connectCallBack;
		if (bluetoothAdapter == null) {
			bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
		}

		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceMac);
		if (device == null) {
			return;
		}

		bluetoothGatt = device.connectGatt(context, isAutoConnect, gattCallback);
		BLELog.e("connectionDevice" + bluetoothGatt.getServices().size());
		bluetoothGatt.connect();

	}

	public BluetoothGatt getBlueToothGatt() {
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

		if (bleConnectCallBack == null) {
			return;
		}

		bleConnectCallBack.onConnecting();
		BLELog.i("BLEConnection --->> onConnecting()");
	}

	@Override
	public void onConnected() {
		super.onConnected();
		if (bleConnectCallBack == null) {
			return;
		}

		bleConnectCallBack.onConnected();
		BLELog.i("BLEConnection --->> onConnected()");
		bluetoothGatt.discoverServices();

	}

	@Override
	public void onDisConnecting() {
		super.onDisConnecting();
		if (bleConnectCallBack == null) {
			return;
		}

		bleConnectCallBack.onDisConnecting();
		BLELog.i("BLEConnection --->> onDisConnecting()");
	}

	@Override
	public void onDisConnected() {
		super.onDisConnected();

		if (bleConnectCallBack == null) {
			return;
		}

		bleConnectCallBack.onDisConnected();
		BLELog.i("BLEConnection --->> onDisConnected()");
	}

	@Override
	public void onBleServerEnable() {
		super.onBleServerEnable();
		if (bleConnectCallBack == null) {
			return;
		}

		bleConnectCallBack.onBleServerEnable();
		BLELog.i("BLEConnection --->> onBleServerEnable()");
	}
}
