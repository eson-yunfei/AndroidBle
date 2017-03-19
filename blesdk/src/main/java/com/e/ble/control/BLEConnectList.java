package com.e.ble.control;

import android.bluetooth.BluetoothGatt;

import com.e.ble.BLESdk;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyunfei
 * @date: 2017/3/19
 * @Description：
 */

class BLEConnectList {
	private static BLEConnectList sBLEConnectList = null;

	private HashMap<String, BluetoothGatt> mGattHashMap;

	private BLEConnectList() {
		if (mGattHashMap == null) {
			mGattHashMap = new HashMap<>();
		}
	}

	public static BLEConnectList get() {
		if (sBLEConnectList == null) {
			sBLEConnectList = new BLEConnectList();
		}
		return sBLEConnectList;
	}

	/**
	 * 是否超出设置的
	 * 最大连接设备个数
	 *
	 * @return
	 */
	public boolean outLimit() {
		if (mGattHashMap.size() >= BLESdk.get().getMaxConnect()) {
			return true;
		}
		return false;
	}

	public void putGatt(String address, BluetoothGatt gatt) {
		mGattHashMap.put(address, gatt);
	}

	public BluetoothGatt getGatt(String address) {

		if (mGattHashMap.containsKey(address)) {
			return mGattHashMap.get(address);
		}
		return null;
	}

	public void disconnectAll() {

		for (Map.Entry<String, BluetoothGatt> gattEntry : mGattHashMap.entrySet()) {
			String key = gattEntry.getKey();
			BluetoothGatt gatt = gattEntry.getValue();

			disconnect(key, gatt);
		}
	}

	public void disconnect(String deviceAddress) {

		BluetoothGatt gatt = getGatt(deviceAddress);
		disconnect(deviceAddress, gatt);
	}

	private void disconnect(String deviceAddress, BluetoothGatt gatt) {

		if (gatt == null) {
			if (mGattHashMap.containsKey(deviceAddress)) {
				mGattHashMap.remove(deviceAddress);
			}
			return;
		}
		mGattHashMap.remove(deviceAddress);
		gatt.disconnect();
		gatt.close();
		gatt = null;

	}

}
