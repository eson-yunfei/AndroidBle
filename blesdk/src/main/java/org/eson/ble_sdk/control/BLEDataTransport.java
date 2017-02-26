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
		if (bleDataTransCallBack != null) {
			dataSendCallBacks.add(bleDataTransCallBack);
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

		if (bleDataTransCallBack != null) {
			dataNotifyCallBacks.add(bleDataTransCallBack);
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
	public void onNotify(byte[] data) {
//		super.onNotify(data);
		if (dataNotifyCallBacks.size() == 0) {
			return;
		}


		for (BLEDataTransCallBack dataNotifyCallBack : dataNotifyCallBacks) {

			dataNotifyCallBack.onNotify(data);
		}
	}

	@Override
	public void removeDataSendCallback(BLEDataTransCallBack dataTransCallBack) {
		super.removeDataSendCallback(dataTransCallBack);
		if (dataTransCallBack == null || dataSendCallBacks.size() == 0) {
			return;
		}

		for (int i = 0; i < dataSendCallBacks.size(); i++) {
			if (dataSendCallBacks.contains(dataTransCallBack)) {
				dataSendCallBacks.remove(dataTransCallBack);
			}
		}
	}

	@Override
	public void removeDataNotifyCallback(BLEDataTransCallBack dataTransCallBack) {
		super.removeDataNotifyCallback(dataTransCallBack);

		if (dataTransCallBack == null || dataNotifyCallBacks.size() == 0) {
			return;
		}

		for (int i = 0; i < dataNotifyCallBacks.size(); i++) {
			if (dataNotifyCallBacks.contains(dataTransCallBack)) {
				dataNotifyCallBacks.remove(dataTransCallBack);
			}
		}

	}

	@Override
	public void cleanDataSendCallback() {
		super.cleanDataSendCallback();
		if (dataSendCallBacks.size() == 0) {
			return;
		}
		dataSendCallBacks.clear();
	}

	@Override
	public void cleanDataNotifyCallback() {
		super.cleanDataNotifyCallback();

		if (dataNotifyCallBacks.size() == 0) {
			return;
		}
		dataNotifyCallBacks.clear();
	}
}
