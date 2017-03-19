package com.e.ble.control;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;

import com.e.ble.bean.BLECharacter;
import com.e.ble.control.listener.BLEConnectionListener;
import com.e.ble.control.listener.BLEReadRssiListener;
import com.e.ble.control.listener.BLEStateChangeListener;
import com.e.ble.control.listener.BLETransportListener;
import com.e.ble.util.BLELog;

import java.util.UUID;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： BaseControl  ，  提供唯一的 BluetoothGattCallback,
 * <p>
 * 根据设备 的 mac 地址 返回不同的数据
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */

abstract class BaseControl implements BLEConnectionListener, BLETransportListener,
		BLEStateChangeListener, BLEReadRssiListener {

	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BluetoothGattCallback 的实例，在程序中保存单个实例,
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */
	protected BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			super.onConnectionStateChange(gatt, status, newState);

			BLELog.i("BaseControl::onConnectionStateChange()" +
					"\n status-->>" + status + ";newState-->>" + newState);
			//更新设备的连接状态
			updateConnectionState(gatt, status, newState);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);
			//回调设备的连接成功或失败
			BLELog.i("BaseControl::onServicesDiscovered()" +
					"\n status-->>" + status);
			deviceConnectionCallback(gatt, status);
		}


		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);

			BLELog.i("BaseControl::onCharacteristicRead()" +
					"\n status-->>" + status);

			if (status == BluetoothGatt.GATT_SUCCESS) {
				BLECharacter bleCharacter = getBleCharacter(gatt, characteristic);
				onCharacterRead(bleCharacter);
			} else {

			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				BLECharacter bleCharacter = getBleCharacter(gatt, characteristic);

				onCharacterWrite(bleCharacter);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);
			// 设备返回数据或设备通知过来数据
			BLECharacter bleCharacter = getBleCharacter(gatt, characteristic);
			onCharacterNotify(bleCharacter);
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorRead(gatt, descriptor, status);

			//TODO
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorWrite(gatt, descriptor, status);
			//TODO
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
			super.onReliableWriteCompleted(gatt, status);
			//TODO
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			super.onReadRemoteRssi(gatt, rssi, status);

			//读取设备的 RSSI 信号值
			String address = getConnectDevice(gatt);
			BLELog.i("BaseControl-->>onReadRemoteRssi() address: " +
					address + " rssi : " + rssi + " status  : " + status);

			if (status == BluetoothGatt.GATT_SUCCESS) {
				onReadRssi(address, rssi);
			} else {
				onReadRssiError(address, status);
			}
		}

		@Override
		public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
			super.onMtuChanged(gatt, mtu, status);
			//TODO
		}
	};


	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEConnectionListener 的接口实现
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	@Override
	public void onConnectError(String address, int errorCode) {
		BLELog.i("BaseControl-->>onConnectError();address: " + address + ";errorCode:" + errorCode);
		BLEConnection.get().onConnectError(address, errorCode);
	}

	@Override
	public void onConnectSuccess(String address) {
		BLELog.i("BaseControl-->>onConnectSuccess();address: " + address);
		BLEConnection.get().onConnectSuccess(address);
	}


	@Override
	public void onConnected(String address) {
		BLELog.i("BaseControl-->>onConnected();address: " + address);
		BLEConnection.get().onConnected(address);
	}


	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLEStateChangeListener 的接口实现
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	@Override
	public void onStateConnected(String address) {

		BLELog.i("BaseControl-->>onStateConnected()");
		BLEConnection.get().onStateConnected(address);
	}

	@Override
	public void onStateConnecting(String address) {
		BLELog.i("BaseControl-->>onStateConnecting()");
		BLEConnection.get().onStateConnecting(address);
	}

	@Override
	public void onStateDisConnecting(String address) {
		BLELog.i("BaseControl-->>onStateDisConnecting()");
		BLEConnection.get().onStateDisConnecting(address);
	}

	@Override
	public void onStateDisConnected(String address) {
		BLELog.i("BaseControl-->>onStateDisConnected()");
		BLEConnection.get().onStateDisConnected(address);
	}


	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * | BLETransportListener 的接口实现
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */
	@Override
	public void onCharacterRead(BLECharacter bleCharacter) {

		BLETransport.get().onCharacterRead(bleCharacter);
	}

	@Override
	public void onCharacterWrite(BLECharacter bleCharacter) {
		BLETransport.get().onCharacterWrite(bleCharacter);
	}

	@Override
	public void onCharacterNotify(BLECharacter bleCharacter) {
		BLETransport.get().onCharacterNotify(bleCharacter);
	}

	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |||||| BaseControl 内部分方法，不提供外部和子类访问
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 更新与设备的连接状态，
	 * GATT_SUCCESS 时调用gatt.discoverServices()，
	 * 不返会设备连接成功，当discoverServices 成功时返回连接成功
	 *
	 * @param gatt
	 * @param status
	 * @param newState
	 */
	private void updateConnectionState(BluetoothGatt gatt, int status, int newState) {

		if (status == BluetoothGatt.GATT_SUCCESS) {
			String address = getConnectDevice(gatt);
			BLELog.d("updateConnectionState()->address:" + address);
			switch (newState) {
				case BluetoothProfile.STATE_CONNECTED:
					//已连接成功
					gatt.discoverServices();
					onStateConnected(address);
					break;
				case BluetoothProfile.STATE_CONNECTING:
					//正在连接
					onStateConnecting(address);
					break;
				case BluetoothProfile.STATE_DISCONNECTED:
					//已断开连接
					onStateDisConnected(address);
					break;
				case BluetoothProfile.STATE_DISCONNECTING:
					//断开连接中
					onStateDisConnecting(address);
					break;
			}

		} else {
			BLELog.e("updateConnectionState()-> status:" + status);
			//发现服务失败，断开设备连接
			onConnectError(getConnectDevice(gatt), status);
			gatt.disconnect();
			gatt.close();
			gatt = null;
		}
	}

	/**
	 * onServicesDiscovered 当服务发现成功时，返回连接成功，
	 * 否则，连接失败，并断开设备的连接
	 *
	 * @param gatt
	 * @param status
	 */
	private void deviceConnectionCallback(BluetoothGatt gatt, int status) {
		if (status == BluetoothGatt.GATT_SUCCESS) {
			BLELog.e("onServicesDiscovered()");
			onConnectSuccess(getConnectDevice(gatt));
		} else {
			BLELog.e("onServicesDiscovered()-> status:" + status);
			onConnectError(getConnectDevice(gatt), status);
		}
	}

	/**
	 * 从连接的 BluetoothGatt 中获取当前设备的 mac 地址
	 *
	 * @param gatt
	 *
	 * @return
	 */
	private String getConnectDevice(BluetoothGatt gatt) {
		String address = "";
		BluetoothDevice device = gatt.getDevice();
		if (device != null) {
			address = device.getAddress();
		}
		return address;
	}

	/**
	 * 组装 BLECharacter
	 *
	 * @param gatt
	 * @param characteristic
	 *
	 * @return
	 */
	private BLECharacter getBleCharacter(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
		String address = getConnectDevice(gatt);
		UUID uuid = characteristic.getUuid();
		byte[] value = characteristic.getValue();

		BLECharacter.BLECharacterBuilder bleCharacterBuilder = new BLECharacter.BLECharacterBuilder(value);
		return bleCharacterBuilder.setDeviceAddress(address).setCharacteristicUUID(uuid).builder();
	}

}
