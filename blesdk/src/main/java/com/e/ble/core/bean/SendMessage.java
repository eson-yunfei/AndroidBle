package com.e.ble.core.bean;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 17:53
 * Package name : com.e.ble.core.bean
 * Des :
 */
public class SendMessage extends AMessage {
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
