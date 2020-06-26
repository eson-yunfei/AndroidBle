package com.e.tool.ble.bean;

import java.util.UUID;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 16:52
 * Package name : com.e.tool.ble.bean
 * Des :
 */
public class NotifyState extends IMsg{
    private boolean enable;
    private UUID desUUID;
    private boolean result;

    public void setDesUUID(UUID des) {
        this.desUUID = des;
    }


    public void setEnable(boolean isListener) {
        this.enable = isListener;
    }

    public UUID getDesUUID() {
        return desUUID;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
