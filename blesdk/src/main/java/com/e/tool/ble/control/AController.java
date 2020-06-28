package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.gatt.BGattCallBack;

import java.util.UUID;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 18:38
 * Package name : com.e.tool.ble.control
 * Des :
 */
abstract class AController {
    protected BleTool bleTool;
    protected BGattCallBack bGattCallBack;

    public AController(BleTool bleTool,
                       BGattCallBack bGattCallBack) {
        this.bleTool = bleTool;
        this.bGattCallBack = bGattCallBack;

    }


    /**
     * 获取指定的 GattCharacteristic
     *
     * @param gatt               gatt
     * @param serviceUuid        serviceUuid
     * @param characteristicUuid characteristicUuid
     * @return BluetoothGattCharacteristic
     */
    protected BluetoothGattCharacteristic getCharacteristicByUUID(BluetoothGatt gatt,
                                                                  UUID serviceUuid,
                                                                  UUID characteristicUuid) {
        if (gatt == null) {
            return null;
        }
        BluetoothGattService service = gatt.getService(serviceUuid);
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(characteristicUuid);
    }


    /**
     * 获取指定的 BluetoothGatt
     *
     * @param address address
     * @return BluetoothGatt
     */
    public BluetoothGatt getGatt(String address) {
        return ((GattCallBack)bGattCallBack).getBluetoothGatt(address);
    }

}
