package org.eson.liteble.bean;

import java.util.List;

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description：
 */

public class ServiceBean {
	private String serviceUUID;

	private List<UUIDBean> mUUIDBeen;

	public String getServiceUUID() {
		return serviceUUID;
	}

	public void setServiceUUID(String serviceUUID) {
		this.serviceUUID = serviceUUID;
	}

	public List<UUIDBean> getUUIDBeen() {
		return mUUIDBeen;
	}

	public void setUUIDBeen(List<UUIDBean> UUIDBeen) {
		mUUIDBeen = UUIDBeen;
	}
}
