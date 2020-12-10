package org.eson.liteble.common.util;

import java.util.UUID;

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description：
 */

public interface UUIDFormat {

	UUID DESC =
			UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

	UUID BATTERY_SERVICE = UUID.fromString("00000180f-0000-1000-8000-00805f9b34fb");
	UUID BATTERY = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");


	UUID DEVICE_INFO_SERVICE = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
	// Manufacturer name
	UUID MANUFACTURER = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");

	// Hardware   302e3031
	UUID HARDWARE = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");

	// firmware   302e3031
	UUID FIRMWARE = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");








}
