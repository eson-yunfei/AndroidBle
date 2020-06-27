package com.e.tool.ble.bean.message;

import java.util.UUID;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 18:53
 * Package name : com.e.tool.ble.bean
 * Des :
 */
abstract class IMsg {

    private UUID serviceUUID;
    private UUID characteristicUUID;
    private String address;

    public UUID getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(UUID serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public UUID getCharacteristicUUID() {
        return characteristicUUID;
    }

    public void setCharacteristicUUID(UUID characteristicUUID) {
        this.characteristicUUID = characteristicUUID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
