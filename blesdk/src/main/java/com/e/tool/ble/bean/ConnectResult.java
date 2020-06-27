package com.e.tool.ble.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/27 11:58
 * Package name : com.e.tool.ble.bean
 * Des :
 */
public class ConnectResult extends DevState implements Serializable {
    private static final long serialVersionUID = -445407285891015581L;
    private boolean servicesDiscovered = false;

    public ConnectResult(@NonNull String address, String name, int status) {
        super(address, name, status);
    }


    public boolean isServicesDiscovered() {
        return servicesDiscovered;
    }

    public void setServicesDiscovered(boolean servicesDiscovered) {
        this.servicesDiscovered = servicesDiscovered;
    }

    @Override
    public String toString() {
        return "ConnectResult{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", newState=" + newState +
                ", servicesDiscovered=" + servicesDiscovered +
                '}';
    }
}
