/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.e.ble.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.e.ble.receiver.listener.BLEReceiverListener;
import com.e.ble.util.BLELog;

/**
 * @author xiaoyunfei
 * @date: 2017/3/15
 * @Description：
 */

public class BLEStateReceiver extends BroadcastReceiver {

	private static BLEReceiverListener mBLEReceiverListener = null;

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
					BluetoothAdapter.ERROR);
			switch (state) {

				case BluetoothAdapter.STATE_OFF:
					BLELog.w("BLEStateReceiver-->>STATE_OFF 手机蓝牙关闭");
					if (mBLEReceiverListener == null) {
						return;
					}
					mBLEReceiverListener.onStateOff();
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					BLELog.w("BLEStateReceiver-->>STATE_TURNING_OFF 手机蓝牙正在关闭");
					if (mBLEReceiverListener == null) {
						return;
					}
					mBLEReceiverListener.onStateStartOff();

					break;
				case BluetoothAdapter.STATE_ON:
					BLELog.w("BLEStateReceiver-->>STATE_ON 手机蓝牙开启");
					if (mBLEReceiverListener == null) {
						return;
					}
					mBLEReceiverListener.onStateOn();
					break;
				case BluetoothAdapter.STATE_TURNING_ON:
					BLELog.w("BLEStateReceiver-->>STATE_TURNING_ON 手机蓝牙正在开启");
					if (mBLEReceiverListener == null) {
						return;
					}
					mBLEReceiverListener.onStateStartOn();
					break;
			}
		}
	}


	public static void setBLEReceiverListener(BLEReceiverListener bleReceiverListener) {
		mBLEReceiverListener = bleReceiverListener;
	}
}
