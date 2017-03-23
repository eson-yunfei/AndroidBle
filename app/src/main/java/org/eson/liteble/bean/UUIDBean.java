package org.eson.liteble.bean;

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description：
 */

public class UUIDBean {
	private String uuid;

	private boolean read = false;
	private boolean write = false;
	private boolean notice = false;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}
}
