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

package com.e.ble.util;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/25
 * @说明：
 */

public class BLEConstant {

	/**
	 * 设备返回数据类型
	 */
	public static final class Type {
		public static final String TYPE_STATE = "ble_state";
		public static final String TYPE_NAME = "ble_mac";
		public static final String TYPE_NOTICE = "ble_notice";
		public static final String TYPE_RISS = "ble_rssi";
		public static final String type_read = "ble_read";
	}

	/**
	 * 设备状态
	 */
	public static final class State {
		public static final int STATE_CONNECTED = 101;
		public static final int STATE_CONNECTING = 102;
		public static final int STATE_DIS_CONNECTED = 103;
		public static final int STATE_DIS_CONNECTING = 104;
	}

	/**
	 * 设备连接是回调
	 */
	public static final class Connection {
		@Deprecated
		public static final int STATE_CONNECT_SUCCEED = 201;
		public static final int STATE_CONNECT_FAILED = 202;
		public static final int STATE_CONNECT_CONNECTED = 203;
	}


}
