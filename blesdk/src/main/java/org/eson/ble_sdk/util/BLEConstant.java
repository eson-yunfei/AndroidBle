package org.eson.ble_sdk.util;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/25
 * @说明：
 */

public class BLEConstant {

	public static final class Type {
		public static final String TYPE_STATE = "ble_state";
		public static final String TYPE_NOTICE = "ble_notice";
	}

	public static final class State {
		public static final int STATE_CONNECTED = 101;
		public static final int STATE_CONNECTING = 102;
		public static final int STATE_DIS_CONNECTED = 103;
		public static final int STATE_DIS_CONNECTING = 104;
		public static final int STATE_DISCOVER_SERVER = 105;

	}

	public static final class BLEData{
		public static final String DATA_UUID = "data_uuid";
		public static final String DATA_VALUE = "data_value";
	}
}
