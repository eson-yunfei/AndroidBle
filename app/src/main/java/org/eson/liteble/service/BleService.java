package org.eson.liteble.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.eson.ble_sdk.bean.BLECharacter;
import org.eson.ble_sdk.bean.BLEUuid;
import org.eson.ble_sdk.control.BLEControl;
import org.eson.ble_sdk.control.listener.BLEConnectionListener;
import org.eson.ble_sdk.control.listener.BLEStateChangeListener;
import org.eson.ble_sdk.control.listener.BLETransportListener;
import org.eson.ble_sdk.util.BLEConstant;
import org.eson.liteble.MyApplication;
import org.eson.liteble.RxBus;
import org.eson.liteble.bean.BleDataBean;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/24
 * @说明：
 */

public class BleService extends Service {

	private static BleService bleService = null;


	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bleService = this;

		BLEControl.get().setBleConnectListener(bleConnectionListener);
		BLEControl.get().setBleStateChangedListener(stateChangeListener);
		BLEControl.get().setBleTransportListener(transportListener);
	}

	public static BleService get() {
		return bleService;
	}

	public void connectionDevice(Context context, String bleMac) {

		BLEControl.get().connectDevice(context, bleMac);
	}

	/**
	 * 启用通知
	 *
	 * @param connectDevice
	 * @param serviceUuid
	 * @param characteristicUuid
	 * @param descriptorUui
	 * @param isListenerNotice
	 */
	public void enableNotify(String connectDevice, UUID serviceUuid, UUID characteristicUuid,
							 UUID descriptorUui, boolean isListenerNotice) {

		BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
				.setAddress(connectDevice)
				.setDescriptorUUID(descriptorUui)
				.setEnable(isListenerNotice).builder();

		BLEControl.get().enableNotify(bleUuid);
	}


	/**
	 * 数据的发送
	 *
	 * @param serviceUuid
	 * @param characteristicUuid
	 * @param bytes
	 */
	public void sendData(UUID serviceUuid, UUID characteristicUuid, byte[] bytes) {
		BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
				.setAddress(MyApplication.getInstance().getCurrentShowDevice())
				.setDataBuffer(bytes).builder();

		BLEControl.get().sendData(bleUuid);
	}


	BLEConnectionListener bleConnectionListener = new BLEConnectionListener() {
		@Override
		public void onConnectError(String address, int errorCode) {
			sendBleState(BLEConstant.Connection.STATE_CONNECT_FAILED, address);

		}

		@Override
		public void onConnectSuccess(String address) {

			//更新当前连接的具体的某一个设备
			MyApplication.getInstance().setCurrentShowDevice(address);
			//添加到已连接的设备列表
//			App.getInstance().addToConnectList(address);
			sendBleState(BLEConstant.Connection.STATE_CONNECT_SUCCEED, address);

		}

		@Override
		public void onConnected(String address) {
			MyApplication.getInstance().setCurrentShowDevice(address);
			sendBleState(BLEConstant.Connection.STATE_CONNECT_CONNECTED, address);
		}
	};


	BLEStateChangeListener stateChangeListener = new BLEStateChangeListener() {
		@Override
		public void onStateConnected(String address) {

		}

		@Override
		public void onStateConnecting(String address) {

		}

		@Override
		public void onStateDisConnecting(String address) {

		}

		@Override
		public void onStateDisConnected(String address) {
			sendBleState(BLEConstant.State.STATE_DIS_CONNECTED,address);
		}
	};

	BLETransportListener transportListener = new BLETransportListener() {
		@Override
		public void onCharacterRead(BLECharacter bleCharacter) {

		}

		@Override
		public void onCharacterWrite(BLECharacter bleCharacter) {

		}

		@Override
		public void onCharacterNotify(BLECharacter bleCharacter) {


			Bundle bundle = new Bundle();


			BleDataBean dataBean = new BleDataBean(bleCharacter.getDeviceAddress(),
					bleCharacter.getCharacteristicUUID(), bleCharacter.getDataBuffer());
			bundle.putSerializable(BLEConstant.Type.TYPE_NOTICE, dataBean);
			RxBus.getInstance().send(bundle);
		}
	};


	/**
	 * 发送蓝牙状态
	 */
	private void sendBleState(int state, String name) {
		Bundle bundle = new Bundle();
		bundle.putInt(BLEConstant.Type.TYPE_STATE, state);
		bundle.putString(BLEConstant.Type.TYPE_NAME, name);
		RxBus.getInstance().send(bundle);
	}
}
