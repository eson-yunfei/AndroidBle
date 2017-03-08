package org.eson.liteble.bean;

import java.io.Serializable;
import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/8
 * @说明：
 */

public class BleDataBean implements Serializable {
	private static final long serialVersionUID = 3942862920612792824L;

	private String deviceAddress;
	private UUID uuid;
	private byte[] buffer;


	public BleDataBean(String deviceAddress, UUID uuid, byte[] buffer) {
		this.deviceAddress = deviceAddress;
		this.uuid = uuid;
		this.buffer = buffer;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
}
