package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/23
 * @说明：
 */

class BLEBaseControl implements BLEConnectCallBack, BLEDataTransCallBack {

	protected BluetoothAdapter bluetoothAdapter = null;
	protected BluetoothGatt bluetoothGatt;

	protected List<BLEConnectCallBack> connectCallBackList = new ArrayList<>();
	protected List<BLEDataTransCallBack> dataSendCallBacks = new ArrayList<>();
	protected List<BLEDataTransCallBack> dataNotifyCallBacks = new ArrayList<>();


	public BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			super.onConnectionStateChange(gatt, status, newState);
			switch (newState) {
				case BluetoothProfile.STATE_CONNECTED:
					//已连接成功
					onConnected();
					break;
				case BluetoothProfile.STATE_CONNECTING:
					//正在连接
					onConnecting();
					break;
				case BluetoothProfile.STATE_DISCONNECTED:
					//已断开连接

					onDisConnected();
					break;
				case BluetoothProfile.STATE_DISCONNECTING:
					//断开连接中
					onDisConnecting();
					break;
			}

		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {

			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);

			if (status == BluetoothGatt.GATT_SUCCESS) {
				//发送成功
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);

			byte[] notice = characteristic.getValue();

			onNotify(notice);
		}
	};


	/**
	 * @param connectCallBack
	 */
	public void removeConnectCallBack(BLEConnectCallBack connectCallBack) {

		BLEConnection.get().removeConnectCallBack(connectCallBack);
	}

	public void removeDataSendCallback(BLEDataTransCallBack dataTransCallBack) {
		BLEDataTransport.get().removeDataSendCallback(dataTransCallBack);

	}

	public void removeDataNotifyCallback(BLEDataTransCallBack dataTransCallBack) {
		BLEDataTransport.get().removeDataNotifyCallback(dataTransCallBack);

	}


	public void cleanConnectCallBack() {

		BLEConnection.get().cleanConnectCallBack();
	}

	public void cleanDataSendCallback() {

		BLEDataTransport.get().cleanDataSendCallback();
	}

	public void cleanDataNotifyCallback() {

		BLEDataTransport.get().cleanDataNotifyCallback();
	}

	///
	@Override

	public void onConnecting() {


	}

	@Override
	public void onCharacteristicRead() {

	}

	@Override
	public void onConnected() {

	}

	@Override
	public void onCharacteristicWrite() {

	}

	@Override
	public void onDisConnecting() {


	}

	@Override
	public void onNotify(byte[] data) {

	}

	@Override
	public void onDisConnected() {

	}
}
