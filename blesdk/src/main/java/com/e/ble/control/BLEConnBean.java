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
