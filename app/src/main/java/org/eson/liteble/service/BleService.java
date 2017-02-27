package org.eson.liteble.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.eson.ble_sdk.control.BLEConnectCallBack;
import org.eson.ble_sdk.control.BLEControl;
import org.eson.ble_sdk.control.BLEDataTransCallBack;
import org.eson.ble_sdk.util.BLEByteUtil;
import org.eson.ble_sdk.util.BLEConstant;
import org.eson.liteble.RxBus;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/24
 * @说明：
 */

public class BleService extends Service {

	private static BleService bleService = null;

//	private BleService() {
//		super();
//	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bleService = this;

	}

	public static BleService get() {
		return bleService;
	}

	public void connectionDevice(Context context, String bleMac) {

		BLEControl.get().connectToDevice(context, bleMac, false, bleConnectCallBack);
	}

	public void enableNotify(UUID serviceUuid, UUID characteristicUuid,
							 UUID descriptorUui, boolean isListenerNotice) {
		if (isListenerNotice) {
			BLEControl.get().enableNotify(serviceUuid, characteristicUuid, descriptorUui, bleDataTransCallBack);
		}else {
			BLEControl.get().disableNotify(serviceUuid, characteristicUuid, descriptorUui, bleDataTransCallBack);
		}
	}


	public void sendData(UUID serviceUuid, UUID characteristicUuid, byte[] bytes) {
		BLEControl.get().sendData(serviceUuid, characteristicUuid, bytes, bleDataTransCallBack);
	}

	BLEConnectCallBack bleConnectCallBack = new BLEConnectCallBack() {
		@Override
		public void onConnecting() {

			sendBleState(BLEConstant.State.STATE_CONNECTING);
		}

		@Override
		public void onConnected() {
			sendBleState(BLEConstant.State.STATE_CONNECTED);
		}

		@Override
		public void onDisConnecting() {
			sendBleState(BLEConstant.State.STATE_DIS_CONNECTING);
		}

		@Override
		public void onDisConnected() {
			sendBleState(BLEConstant.State.STATE_DIS_CONNECTED);
		}

		@Override
		public void onBleServerEnable() {
			sendBleState(BLEConstant.State.STATE_DISCOVER_SERVER);

		}
	};


	BLEDataTransCallBack bleDataTransCallBack = new BLEDataTransCallBack() {
		@Override
		public void onCharRead(String uuid, byte[] data) {

		}

		@Override
		public void onCharWrite(String uuid, byte[] data) {

		}

		@Override
		public void onNotify(String uuid, byte[] data) {

			Bundle bundle = new Bundle();
			bundle.putInt(BLEConstant.Type.TYPE_NOTICE, 0);
			bundle.putString(BLEConstant.BLEData.DATA_UUID, uuid);
			bundle.putString(BLEConstant.BLEData.DATA_VALUE, BLEByteUtil.getHexString(data));
			RxBus.getInstance().send(bundle);

//			BLEByteUtil.printHex(data);
//			LogUtil.e("onNotify()--->>" + BLEByteUtil.getHexString(data));
//			ToastUtil.showShort(MyApplication.getContext(), BLEByteUtil.getHexString(data));


		}
	};

	/**
	 * 发送蓝牙状态
	 */
	private void sendBleState(int state) {
		Bundle bundle = new Bundle();
		bundle.putInt(BLEConstant.Type.TYPE_STATE, state);
		RxBus.getInstance().send(bundle);
	}
}
