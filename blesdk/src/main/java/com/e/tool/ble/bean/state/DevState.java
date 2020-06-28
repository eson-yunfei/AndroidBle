package com.e.tool.ble.bean.state;

import androidx.annotation.NonNull;

import com.e.tool.ble.annotation.LinkState;

import java.io.Serializable;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 16:23
 * Package name : com.e.tool.ble.bean
 * Des :
 */
public class DevState implements Serializable {

    private static final long serialVersionUID = -5777315865251518194L;
    protected String address;
    protected String name;

    protected int status;

    @LinkState
    protected int newState;


    public DevState(@NonNull String address,
                    String name, int status) {
        this.address = address;
        this.name = name;
        this.status = status;
    }

    public void setNewState(int newState) {
        this.newState = newState;
    }

    public int getStatus() {
        return status;
    }

    @LinkState
    public int getNewState() {
        return newState;
    }


    public String getAddress() {
        return address;
    }


    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "DevState{" +
                "status=" + status +
                ", newState=" + newState +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
