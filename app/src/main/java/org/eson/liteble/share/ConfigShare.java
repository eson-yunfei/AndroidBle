package org.eson.liteble.share;

import android.content.Context;

import org.eson.liteble.util.CommonShare;

/**
 * @ author       xiaoyunfei
 * @ date:        2017/3/9
 * @ Description：
 */

public class ConfigShare extends CommonShare {


	private static String SHARE_NAME = "lite_ble_config";
	private String PERMIT_CONNECT_MORE = "permit_connect_more";
	private String CONNECT_TIME = "connect_time";
	private String MAX_CONNECT = "max_connect";


	public ConfigShare(Context context) {
		super(context, SHARE_NAME);
	}


	public boolean isPermitConnectMore() {
		return getValue(PERMIT_CONNECT_MORE, false);
	}

	public void setPermitConnectMore(boolean permitConnectMore) {

		setValue(PERMIT_CONNECT_MORE, permitConnectMore);
	}

	public int getConnectTime() {
		return getValue(CONNECT_TIME, 5000);
	}

	public void setConnectTime(int connectTime) {
		setValue(CONNECT_TIME, connectTime);
	}

	public int getMaxConnect() {
		return getValue(MAX_CONNECT, 1);
	}

	public void setMaxConnect(int maxConnect) {
		setValue(MAX_CONNECT, maxConnect);
	}
}
