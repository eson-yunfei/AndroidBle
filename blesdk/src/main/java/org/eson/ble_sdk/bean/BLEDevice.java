package org.eson.ble_sdk.bean;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */

public class BLEDevice {

	private String name;
	private String mac;
	private int rssi;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	@Override
	public boolean equals(Object obj) {
		BLEDevice device = null;
		if (obj instanceof BLEDevice) {
			device = (BLEDevice) obj;
			if (device.mac.equals(this.mac)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
