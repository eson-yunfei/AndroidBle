package org.eson.liteble.bean;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.bean
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 15:40
 * @change
 * @chang time
 * @class describe
 */
public class BleItem  {

    private String name;    // 设备名字
    private String address; // Mac地址
    private int rssi;       // 蓝牙信号强度

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
