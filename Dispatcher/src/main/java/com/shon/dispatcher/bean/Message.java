package com.shon.dispatcher.bean;

import java.util.Arrays;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 17:10
 * Package name : com.shon.dispatcher.bean
 * Des :
 */
public abstract class Message {
    private byte[] bytes;

    private Object object;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Message{" +
                "bytes=" + Arrays.toString(bytes) +
                ", object=" + object +
                '}';
    }
}