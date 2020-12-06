package com.shon.bluetooth.core;

import androidx.annotation.NonNull;

import com.shon.bluetooth.core.annotation.ResultType;

import java.util.Arrays;

/**
 * 蓝牙返回数据
 */
public class Result {
    private String address;
    private String uuid;
    private byte[] bytes;
    private final int type;

    public Result(@ResultType int type) {
        this.type = type;
    }

    @ResultType
    public int getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "address='" + address + '\'' +
                ", uuid='" + uuid + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }
}
