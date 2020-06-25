package com.e.ble.core.bean;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:40
 * Package name : com.e.ble.core.bean
 * Des :
 */
public class ReadMessage extends AMessage {

    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
