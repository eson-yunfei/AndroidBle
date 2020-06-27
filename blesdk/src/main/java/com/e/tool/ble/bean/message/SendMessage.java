package com.e.tool.ble.bean.message;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 17:53
 * Package name : com.e.tool.ble.bean
 * Des :
 */
public class SendMessage extends IMsg {
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
