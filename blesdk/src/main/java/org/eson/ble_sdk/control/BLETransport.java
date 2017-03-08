package org.eson.ble_sdk.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import org.eson.ble_sdk.BLESdk;
import org.eson.ble_sdk.bean.BLECharacter;
import org.eson.ble_sdk.bean.BLEUuid;
import org.eson.ble_sdk.control.listener.BLETransportListener;
import org.eson.ble_sdk.util.BLEByteUtil;
import org.eson.ble_sdk.util.BLELog;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明：
 */

class BLETransport implements BLETransportListener {

	/**
	 * |----------------------------------------------------------------------|
	 * |                                                                      |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |
	 * |<p>  实例化
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |																	  |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 */
	private static BLETransport bleTransport = null;
	private BLETransportListener bleTransportListener = null;
	private BluetoothAdapter bluetoothAdapter;

	private BLETransport() {
		bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
	}

	public static BLETransport get() {
		if (bleTransport == null) {
			bleTransport = new BLETransport();
		}
		return bleTransport;
	}

	/**
	 * |----------------------------------------------------------------------|
	 * |                                                                      |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |
	 * |<p>  API
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |																	  |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 */


	/**
	 * 设置回调
	 *
	 * @param bleTransportListener
	 */
	public void setBleTransportListener(BLETransportListener bleTransportListener) {
		this.bleTransportListener = bleTransportListener;
	}

	/**
	 * 通知的启用和关闭
	 *
	 * @param bluetoothGatt
	 * @param bleUuid
	 */
	public void enableNotify(BluetoothGatt bluetoothGatt, BLEUuid bleUuid) {

		if (checkGattEnable(bluetoothGatt)) {
			return;
		}

		if (!isConnect(bluetoothGatt, bleUuid.getAddress())) {
			return;
		}

		UUID serviceUuid = bleUuid.getServiceUUID();
		UUID characteristicUuid = bleUuid.getCharacteristicUUID();
		BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(bluetoothGatt, serviceUuid, characteristicUuid);
		if (characteristic == null) {
			return;
		}
		UUID descriptorUuid = bleUuid.getDescriptorUUID();
		changeNotifyState(bluetoothGatt, characteristic, descriptorUuid, bleUuid.isEnable());
		BLELog.e("enableNotify()-->>characteristicUuid:" + characteristicUuid);
	}


	/**
	 * 发送数据
	 *
	 * @param bluetoothGatt
	 * @param bleUuid
	 */
	public void sendDataToDevice(BluetoothGatt bluetoothGatt, BLEUuid bleUuid) {
		if (checkGattEnable(bluetoothGatt)) {
			return;
		}

		if (!isConnect(bluetoothGatt, bleUuid.getAddress())) {
			return;
		}


		UUID serviceUuid = bleUuid.getServiceUUID();
		UUID characteristicUuid = bleUuid.getCharacteristicUUID();
		BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(bluetoothGatt, serviceUuid, characteristicUuid);
		if (characteristic == null) {
			return;
		}
		BLELog.e("发送数据-->>:");
		BLEByteUtil.printHex(bleUuid.getDataBuffer());
		characteristic.setValue(bleUuid.getDataBuffer());
		bluetoothGatt.writeCharacteristic(characteristic);
	}

	/**
	 * 读取数据
	 *
	 * @param bluetoothGatt
	 * @param bleUuid
	 */
	public void readDeviceData(BluetoothGatt bluetoothGatt, BLEUuid bleUuid) {

		if (checkGattEnable(bluetoothGatt)) {
			return;
		}

		if (!isConnect(bluetoothGatt, bleUuid.getAddress())) {
			return;
		}
		UUID serviceUuid = bleUuid.getServiceUUID();
		UUID characteristicUuid = bleUuid.getCharacteristicUUID();

		BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(bluetoothGatt, serviceUuid, characteristicUuid);
		if (characteristic == null) {
			return;
		}
		bluetoothGatt.readCharacteristic(characteristic);
	}
/**
 * |----------------------------------------------------------------------|
 * |                                                                      |
 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
 * |																	  |
 * |----------------------------------------------------------------------|
 * |
 * |<p>  私有方法，
 * |																	  |
 * |----------------------------------------------------------------------|
 * |																	  |
 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
 * |																	  |
 * |----------------------------------------------------------------------|
 */
	/**
	 * 检测 bluetoothGatt 是否可用
	 *
	 * @return
	 */
	private boolean checkGattEnable(BluetoothGatt bluetoothGatt) {
		if (bluetoothGatt == null) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isConnect(BluetoothGatt gatt, String deviceAddress) {

		boolean isConnect = false;

		//bluetoothGatt 为null
		if (gatt == null) {
			return isConnect;
		}
		if (bluetoothAdapter == null) {
			return isConnect;
		}
		BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

		int state = BLESdk.get().getBluetoothManager().getConnectionState(bluetoothDevice, BluetoothProfile.GATT);
		if (state == BluetoothGatt.STATE_CONNECTED) {
			isConnect = true;
		}

		return isConnect;
	}

	/**
	 * 获取指定的 GattCharacteristic
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 *
	 * @return
	 */
	private BluetoothGattCharacteristic getCharacteristicByUUID(BluetoothGatt bluetoothGatt, UUID serviceUuid, UUID characteristicUuid) {
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
	private void changeNotifyState(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic, UUID descriptorUuid, boolean enable) {
		bluetoothGatt.setCharacteristicNotification(characteristic, enable);//激活通知
//		BLELog.e("descriptorUuid-->>" + descriptorUuid.toString());

		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUuid);
		if (descriptor == null) {
			BLELog.e("descriptorUuid not find");
			return;
		}
		if (enable) {
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		} else {
			descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		}
		bluetoothGatt.writeDescriptor(descriptor);
	}

	/**
	 * |----------------------------------------------------------------------|
	 * |                                                                      |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |
	 * |<p>  BLETransportListener 回调
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |																	  |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 */

	@Override
	public void onCharacterRead(BLECharacter bleCharacter) {

		if (bleTransportListener != null) {
			bleTransportListener.onCharacterRead(bleCharacter);
		}
	}

	@Override
	public void onCharacterWrite(BLECharacter bleCharacter) {
		if (bleTransportListener != null) {
			bleTransportListener.onCharacterWrite(bleCharacter);
		}
	}

	@Override
	public void onCharacterNotify(BLECharacter bleCharacter) {
		BLELog.e("BLETransport -->>onCharacterNotify()");
		if (bleTransportListener != null) {
			bleTransportListener.onCharacterNotify(bleCharacter);
		}
	}
}
