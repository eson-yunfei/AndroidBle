package org.eson.liteble.activity.bean;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 15:25
 * Package name : org.eson.liteble.activity.bean
 * Des :
 */
public class BondedDeviceBean {
    private String address;
    private String name;
    private boolean connected = false;
    private boolean connecting = false;

    public BondedDeviceBean(String address){
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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnecting() {
        return connecting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    @Override
    public String toString() {
        return "BondedDeviceBean{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", connected=" + connected +
                ", connecting=" + connecting +
                '}';
    }
}
