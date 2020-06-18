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

package com.e.ble.bean;

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
