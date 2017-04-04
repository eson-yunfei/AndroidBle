package org.eson.liteble.bean;

import java.util.List;

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description： Service UUID
 */

public class ServiceBean {
    private String serviceUUID;

    private List<CharacterBean> mUUIDBeen;

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public List<CharacterBean> getUUIDBeen() {
        return mUUIDBeen;
    }

    public void setUUIDBeen(List<CharacterBean> UUIDBeen) {
        mUUIDBeen = UUIDBeen;
    }
}
