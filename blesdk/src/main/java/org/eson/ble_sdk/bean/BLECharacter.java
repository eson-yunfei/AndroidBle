package org.eson.ble_sdk.bean;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明：
 */

public class BLECharacter {

	private UUID characteristicUUID;
	private byte[] dataBuffer;
	private String deviceAddress;

	public BLECharacter(byte[] dataBuffer) {
		this.dataBuffer = dataBuffer;
	}

	public UUID getCharacteristicUUID() {
		return characteristicUUID;
	}

	public void setCharacteristicUUID(UUID characteristicUUID) {
		this.characteristicUUID = characteristicUUID;
	}

	public byte[] getDataBuffer() {
		return dataBuffer;
	}


	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	/**
	 * |----------------------------------------------------------------------|
	 * |                                                                      |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |
	 * |<p>  class BLEUuidBuilder
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 * |																	  |
	 * |++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
	 * |																	  |
	 * |----------------------------------------------------------------------|
	 */

	public static class BLECharacterBuilder {
		private BLECharacter bleCharacter = null;

		public BLECharacterBuilder(byte[] dataBuffer) {
			bleCharacter = new BLECharacter(dataBuffer);
		}

		public BLECharacterBuilder setCharacteristicUUID(UUID characteristicUUID) {
			bleCharacter.setCharacteristicUUID(characteristicUUID);
			return this;
		}

		public BLECharacterBuilder setDeviceAddress(String deviceAddress) {
			bleCharacter.setDeviceAddress(deviceAddress);
			return this;
		}


		public BLECharacter builder() {
			return bleCharacter;
		}

	}
}
