package com.e.tool.ble.bean.message;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:40
 * Package name : com.e.tool.ble.bean
 * Des :
 */
public class ReadMessage extends IMsg {

    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
