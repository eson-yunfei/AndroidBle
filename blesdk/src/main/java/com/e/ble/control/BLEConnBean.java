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

package com.e.ble.control;

import android.bluetooth.BluetoothGatt;

/**
 * @author xiaoyunfei
 * @date: 2017/3/23
 * @Description：
 */

class BLEConnBean {
	private String address;
	private BluetoothGatt mGatt;
	private BaseControl.BLEGattCallBack mBLEGattCallBack;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BluetoothGatt getGatt() {
		return mGatt;
	}

	public void setGatt(BluetoothGatt gatt) {
		mGatt = gatt;
	}

	public BaseControl.BLEGattCallBack getBLEGattCallBack() {
		return mBLEGattCallBack;
	}

	public void setBLEGattCallBack(BaseControl.BLEGattCallBack BLEGattCallBack) {
		mBLEGattCallBack = BLEGattCallBack;
	}
}
