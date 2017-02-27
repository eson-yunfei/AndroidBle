package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;

import org.eson.ble_sdk.util.BLEByteUtil;
import org.eson.ble_sdk.util.BLELog;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/23
 * @说明：
 */

class BLEBaseControl implements BLEConnectCallBack, BLEDataTransCallBack {

	protected BluetoothAdapter bluetoothAdapter = null;
	protected BluetoothGatt bluetoothGatt;

	protected BLEDataTransCallBack dataTransCallBack = null;

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

			onBleServerEnable();
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {

				BLELog.d("-->>onCharRead()");
				UUID uuid = characteristic.getUuid();

				byte[] readValue = characteristic.getValue();
				onCharRead(uuid.toString(), readValue);
			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);

			if (status == BluetoothGatt.GATT_SUCCESS) {
				//发送成功
				BLELog.d("-->>onCharWrite()");
				UUID uuid = characteristic.getUuid();

				byte[] writeValue = characteristic.getValue();

				onCharWrite(uuid.toString(), writeValue);
				BLELog.d("uuid-->>" + uuid.toString());
				BLEByteUtil.printHex(writeValue);
			}
		}


		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);

			BLELog.d("-->>onCharacteristicChanged()");
			if (characteristic == null) {
				return;
			}
			UUID uuid = characteristic.getUuid();
			BLELog.d("uuid-->>" + uuid.toString());

			byte[] noticeValue = characteristic.getValue();
			onNotify(uuid.toString(), noticeValue);
		}
	};


	///
	@Override

	public void onConnecting() {


	}

	@Override
	public void onCharRead(String uuid, byte[] data) {
		BLEDataTransport.get().onCharRead(uuid, data);
	}

	@Override
	public void onConnected() {

	}

	@Override
	public void onCharWrite(String uuid, byte[] data) {
		BLEDataTransport.get().onCharWrite(uuid, data);
	}

	@Override
	public void onDisConnecting() {

	}

	@Override
	public void onNotify(String uuid, byte[] data) {
		BLEDataTransport.get().onNotify(uuid, data);
	}

	@Override
	public void onDisConnected() {

	}

	@Override
	public void onBleServerEnable() {

	}
}
