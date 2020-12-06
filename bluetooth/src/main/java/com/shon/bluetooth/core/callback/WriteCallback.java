package com.shon.bluetooth.core.callback;

import com.shon.bluetooth.util.BleLog;

/**
 * Auth : xiao.yunfei
 * Date : 2020/10/05 19:28
 * Package name : com.shon.bluetooth.contorller.imp
 * Des :
 */
public abstract class WriteCallback implements ICallback, OnTimeout {

    protected String mac;
    public WriteCallback(String address){
        this.mac = address;
    }

    public String getAddress() {
        return mac;
    }



    /**
     * @return 是否移除 callback
     */
    public boolean removeOnWriteSuccess() {
        BleLog.d("数据发送成功");
        return false;
    }

    public abstract byte[] getSendData();

    public boolean isFinish() {
        return true;
    }
}
