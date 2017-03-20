package com.e.ble.control;

import android.bluetooth.BluetoothGatt;
import android.os.DeadObjectException;

import com.e.ble.BLESdk;
import com.e.ble.util.BLELog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @author xiaoyunfei
 * @date: 2017/3/19
 * @Description： BLEConnectList  连接的设备列表的管理类
 * <p>
 * 提供添加，删除，断裂连接
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
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

	/**
	 * 添加新的连接设备
	 *
	 * @param address
	 * @param gatt
	 */
	public void putGatt(String address, BluetoothGatt gatt) {
		mGattHashMap.put(address, gatt);
	}

	/**
	 * 根据设备 mac 获取 BluetoothGatt
	 *
	 * @param address
	 *
	 * @return
	 */
	public BluetoothGatt getGatt(String address) {

		if (mGattHashMap.containsKey(address)) {
			return mGattHashMap.get(address);
		}
		return null;
	}

	/**
	 * 断开所有的设备连接
	 */
	public void disconnectAll() {

		for (Map.Entry<String, BluetoothGatt> gattEntry : mGattHashMap.entrySet()) {
			String key = gattEntry.getKey();
			BluetoothGatt gatt = gattEntry.getValue();
			try {
				disconnect(key, gatt);
			} catch (Exception e) {
				BLELog.e("deviceAddress:" + key + " ; gatt is error");
			}

		}
	}

	/**
	 * 断开某一个设备连接
	 *
	 * @param deviceAddress
	 */
	public void disconnect(String deviceAddress) {

		BLELog.e("BLEConnectList :: disconnect() deviceAddress::" + deviceAddress);
		BluetoothGatt gatt = getGatt(deviceAddress);
		try {
			disconnect(deviceAddress, gatt);
		} catch (Exception e) {
			BLELog.e("deviceAddress:" + deviceAddress + " ; gatt is error");
		}

	}


	/**
	 * 断开设备连接
	 *
	 * @param deviceAddress
	 * @param gatt
	 */
	private void disconnect(String deviceAddress, BluetoothGatt gatt) throws DeadObjectException {

		if (gatt == null) {
			if (mGattHashMap.containsKey(deviceAddress)) {
				mGattHashMap.remove(deviceAddress);
			}
			return;
		}
		mGattHashMap.remove(deviceAddress);

		BLELog.e("disconnect() deviceAddress ::" + deviceAddress);
		try {
			BLELog.e("disconnect() close gatt ::");

			gatt.disconnect();
			refreshCache(gatt);
			gatt.close();
			gatt.close();
			BLELog.e("disconnect() close gatt :: finish ");
		} catch (Exception e) {
			BLELog.e("deviceAddress:" + deviceAddress + " ; gatt is error");
		}


	}

	private void refreshCache(BluetoothGatt gatt) throws DeadObjectException {

		try {
			Method refresh = gatt.getClass().getMethod("refresh");
			if (refresh != null) {
				BLELog.e("refreshCache ::");
				refresh.invoke(gatt);
			}
		} catch (Exception e) {
			BLELog.e(e.getMessage());
		}
	}

	public void removeGatt(String address) {
		if (mGattHashMap.containsKey(address)) {
			mGattHashMap.remove(address);
		}
	}

	public void cleanGatt() {
		mGattHashMap.clear();
	}
}
