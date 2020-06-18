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

/*
  @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明：
 */


/**
 * ----------------------------------------------------------------------|
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
 * <p>  class BLEUuidBuilder
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|
 * ----------------------------------------------------------------------|
 */
public class BLEUuid {
	private UUID serviceUUID;
	private UUID characteristicUUID;

	private UUID descriptorUUID;        //用于关闭和启用通知
	private boolean enable = true;        //是否开启服务
	private byte[] dataBuffer;

	private String address;

	public BLEUuid(UUID serviceUUID, UUID characterUUID) {

		this.serviceUUID = serviceUUID;
		this.characteristicUUID = characterUUID;
	}

	public UUID getServiceUUID() {
		return serviceUUID;
	}

	public void setServiceUUID(UUID serviceUUID) {
		this.serviceUUID = serviceUUID;
	}

	public UUID getCharacteristicUUID() {
		return characteristicUUID;
	}

	public void setCharacteristicUUID(UUID characteristicUUID) {
		this.characteristicUUID = characteristicUUID;
	}

	public UUID getDescriptorUUID() {
		return descriptorUUID;
	}

	public void setDescriptorUUID(UUID descriptorUUID) {
		this.descriptorUUID = descriptorUUID;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public byte[] getDataBuffer() {
		return dataBuffer;
	}

	public void setDataBuffer(byte[] dataBuffer) {
		this.dataBuffer = dataBuffer;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	public static class BLEUuidBuilder {

		private BLEUuid bleUuid = null;

		public BLEUuidBuilder(UUID serviceUUID, UUID characterUUID) {
			bleUuid = new BLEUuid(serviceUUID, characterUUID);
		}

		public BLEUuidBuilder setDescriptorUUID(UUID descriptorUUID) {
			bleUuid.setDescriptorUUID(descriptorUUID);
			return this;
		}

		public BLEUuidBuilder setEnable(boolean enable) {
			bleUuid.setEnable(enable);
			return this;
		}

		public BLEUuidBuilder setDataBuffer(byte[] dataBuffer) {
			bleUuid.setDataBuffer(dataBuffer);
			return this;
		}

		public BLEUuidBuilder setAddress(String address) {
			bleUuid.setAddress(address);
			return this;
		}

		public BLEUuid builder() {
			return bleUuid;
		}
	}
}

