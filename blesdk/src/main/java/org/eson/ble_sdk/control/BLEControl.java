package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;

import org.eson.ble_sdk.BLESdk;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public class BLEControl {

	private BLEControl() {

	}

	private static BLEControl bleControl = null;


	public static void init() {
		if (bleControl == null) {
			bleControl = new BLEControl();
		}
	}

	public static BLEControl get() {
		init();
		return bleControl;
	}

	private BluetoothAdapter bluetoothAdapter = null;
	private BluetoothGatt bluetoothGatt;

	/**
	 * 连接设备
	 *
	 * @param context
	 * @param deviceMac
	 * @param isAutoConnect
	 */
	public void connectToDevice(Context context, String deviceMac, boolean isAutoConnect) {

		if (TextUtils.isEmpty(deviceMac)) {
			return;
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
		bluetoothGatt.connect();
	}

	BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			super.onConnectionStateChange(gatt, status, newState);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);

			if (status == BluetoothGatt.GATT_SUCCESS){
				//发送成功
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);

			byte[] notice = characteristic.getValue();


		}
	};


	/**
	 * 发送数据
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 * @param data
	 */
	public void sendData(UUID serviceUuid, UUID characteristicUuid, byte[] data) {

		if (bluetoothGatt == null) {
			return;
		}
		BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
		if (service == null) {
			return;
		}
		BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
		if (characteristic == null) {
			return;
		}
		characteristic.setValue(data);
		bluetoothGatt.writeCharacteristic(characteristic);
	}


	/**
	 * 激活通知服务
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 * @param descriptorUuid
	 */
	public void enableNotify(UUID serviceUuid, UUID characteristicUuid, UUID descriptorUuid) {

		if (bluetoothGatt == null) {
			return;
		}

		BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
		if (service == null) {
			return;
		}

		BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
		if (characteristic == null) {
			return;
		}

		bluetoothGatt.setCharacteristicNotification(characteristic, true);//激活通知

		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUuid);
		descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		bluetoothGatt.writeDescriptor(descriptor);

	}
}
