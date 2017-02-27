package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import org.eson.ble_sdk.util.BLELog;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/23
 * @说明： 蓝牙的数据交换，包括 发送数据，读取数据，通知类数据
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

	//*****************************************************************************************************//
	//发送数据
	//*****************************************************************************************************//

	/**
	 * 发送数据
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 * @param data
	 * @param bleDataTransCallBack
	 */
	public void sendData(UUID serviceUuid, UUID characteristicUuid, byte[] data, BLEDataTransCallBack bleDataTransCallBack) {
		if (checkGattEnable()) return;
		//TODO 判断蓝牙连接状态
		if (bleDataTransCallBack != null && dataTransCallBack == null) {
			dataTransCallBack = bleDataTransCallBack;
		}

		BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(serviceUuid, characteristicUuid);
		if (characteristic == null) {
			return;
		}
		characteristic.setValue(data);
		bluetoothGatt.writeCharacteristic(characteristic);
	}

	//*****************************************************************************************************//
	//*****************************************************************************************************//
	public void readData(UUID serviceUuid, UUID characteristicUuid) {
		if (checkGattEnable()) return;
		//TODO 判断蓝牙连接状态

		BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(serviceUuid, characteristicUuid);
		if (characteristic == null) {
			return;
		}
		bluetoothGatt.readCharacteristic(characteristic);
	}

	//*****************************************************************************************************//
	//*****************************************************************************************************//
	public void enableNotify(UUID serviceUuid, UUID characteristicUuid, UUID descriptorUuid, BLEDataTransCallBack bleDataTransCallBack) {

		if (checkGattEnable()) return;

		//TODO 判断蓝牙连接状态

		if (bleDataTransCallBack != null && dataTransCallBack == null) {
			dataTransCallBack = bleDataTransCallBack;
		}

		BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(serviceUuid, characteristicUuid);
		if (characteristic == null) {
			return;
		}

		changeNotifyState(characteristic, descriptorUuid, true);    //启用通知

		BLELog.i("enableNotify-->>" + characteristicUuid.toString());
	}


	public void disableNotify(UUID serviceUuid, UUID characteristicUuid, UUID descriptorUuid, BLEDataTransCallBack bleDataTransCallBack) {
		if (checkGattEnable()) return;

		//TODO 判断蓝牙连接状态

		if (bleDataTransCallBack != null && dataTransCallBack == null) {
			dataTransCallBack = bleDataTransCallBack;
		}
		BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(serviceUuid, characteristicUuid);
		if (characteristic == null) {
			return;
		}

		changeNotifyState(characteristic, descriptorUuid, false);    //关闭通知
		BLELog.i("disableNotify-->>" + characteristicUuid.toString());
	}

	//*****************************************************************************************************//
	//*****************************************************************************************************//

	/**
	 * 检测 bluetoothGatt 是否可用
	 *
	 * @return
	 */
	private boolean checkGattEnable() {
		if (bluetoothGatt == null) {
			bluetoothGatt = BLEControl.get().getBluetoothGatt();
		}
		if (bluetoothGatt == null) {
			return true;
		}
		return false;
	}

	/**
	 * 获取指定的 GattCharacteristic
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 *
	 * @return
	 */
	private BluetoothGattCharacteristic getCharacteristicByUUID(UUID serviceUuid, UUID characteristicUuid) {
		BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
		if (service == null) {
			return null;
		}
		return service.getCharacteristic(characteristicUuid);
	}

	/**
	 * 改变通知状态
	 *
	 * @param characteristic
	 * @param descriptorUuid
	 * @param enable
	 */
	private void changeNotifyState(BluetoothGattCharacteristic characteristic, UUID descriptorUuid, boolean enable) {
		bluetoothGatt.setCharacteristicNotification(characteristic, enable);//激活通知

		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUuid);
		if (enable) {
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		} else {
			descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		}
		bluetoothGatt.writeDescriptor(descriptor);
	}

	//*****************************************************************************************************//
	//*****************************************************************************************************//
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
