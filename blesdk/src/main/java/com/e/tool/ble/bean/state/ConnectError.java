package com.e.tool.ble.bean.state;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 14:35
 * Package name : com.e.tool.ble.bean.state
 * Des :
 */
public class ConnectError {
    private String address;
    private String name;
    private int status;

    public ConnectError(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConnectError{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
