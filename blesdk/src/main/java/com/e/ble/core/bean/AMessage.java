package com.e.ble.core.bean;

import java.util.UUID;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:38
 * Package name : com.e.ble.core.bean
 * Des :
 */
abstract class AMessage {

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
