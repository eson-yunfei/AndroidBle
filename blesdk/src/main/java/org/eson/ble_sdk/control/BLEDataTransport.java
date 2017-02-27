package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import org.eson.ble_sdk.util.BLELog;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/23
 * @说明：
 */

class BLEDataTransport extends BLEBaseControl {

	private BLEDataTransport() {

	}

	private static BLEDataTransport bleDataTransport = null;

	public static BLEDataTransport get() {
		if (bleDataTransport == null) {
			bleDataTransport = new BLEDataTransport();

		}
		return bleDataTransport;
	}


	public void sendData(UUID serviceUuid, UUID characteristicUuid, byte[] data, BLEDataTransCallBack bleDataTransCallBack) {
		if (bluetoothGatt == null) {
			bluetoothGatt = BLEControl.get().getBluetoothGatt();
		}
		if (bluetoothGatt == null) {
			return;
		}

		//TODO 判断蓝牙连接状态

		//
		if (bleDataTransCallBack != null && dataTransCallBack == null) {
			dataTransCallBack = bleDataTransCallBack;
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

	public void readData(UUID serviceUuid, UUID characteristicUuid) {
		if (bluetoothGatt == null) {
			bluetoothGatt = BLEControl.get().getBluetoothGatt();
		}
		if (bluetoothGatt == null) {
			return;
		}
		//TODO 判断蓝牙连接状态

		BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
		if (service == null) {
			return;
		}
		BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
		if (characteristic == null) {
			return;
		}
		bluetoothGatt.readCharacteristic(characteristic);
	}

	public void enableNotify(UUID serviceUuid, UUID characteristicUuid, UUID descriptorUuid, BLEDataTransCallBack bleDataTransCallBack) {

		if (bluetoothGatt == null) {
			bluetoothGatt = BLEControl.get().getBluetoothGatt();
		}

		if (bluetoothGatt == null) {
			return;
		}

		//TODO 判断蓝牙连接状态

		if (bleDataTransCallBack != null && dataTransCallBack == null) {
			dataTransCallBack = bleDataTransCallBack;
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

		BLELog.i("enableNotify-->>" + characteristicUuid.toString());
	}

	@Override
	public void onCharWrite(String uuid, byte[] data) {
		if (dataTransCallBack != null) {
			dataTransCallBack.onCharWrite(uuid, data);
		}
	}

	@Override
	public void onCharRead(String uuid, byte[] data) {
		if (dataTransCallBack != null) {
			dataTransCallBack.onCharRead(uuid, data);
		}
	}

	@Override
	public void onNotify(String uuid, byte[] data) {
		if (dataTransCallBack != null) {
			dataTransCallBack.onNotify(uuid, data);
		}
	}

}
