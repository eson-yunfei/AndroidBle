package com.shon.bluetooth.core.gatt;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.shon.bluetooth.DataDispatcher;
import com.shon.bluetooth.core.Result;
import com.shon.bluetooth.core.annotation.Constants;
import com.shon.bluetooth.core.callback.WriteCallback;
import com.shon.bluetooth.util.BleLog;
import com.shon.bluetooth.util.ByteUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/9/28 16:25
 * Package name : com.shon.bluetooth.core.gatt
 * Des :
 */
public class CharacteristicGattCallback  {
    private final DataDispatcher dataDispatcher;
    public CharacteristicGattCallback(DataDispatcher dataDispatcher){
        this.dataDispatcher = dataDispatcher;
    }
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS){
            return;
        }
        BleLog.d("CharacteristicGattCallback :: onDescriptorWrite ");
        byte[] value = descriptor.getValue();
        String hexString = ByteUtil.getHexString(value);
        String address = gatt.getDevice().getAddress();

        Result result = new Result(Constants.PROPERTY_NOTIFY);
        result.setAddress(address);
        result.setBytes(value);
        dataDispatcher.onReceivedResult(result);

    }

    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS){
            return;
        }
        BleLog.d("CharacteristicGattCallback :: onCharacteristicRead ");
        String address = gatt.getDevice().getAddress();
        String uuid = characteristic.getUuid().toString().toLowerCase();
        byte[] value = characteristic.getValue();

        Result result = new Result(Constants.PROPERTY_READ);
        result.setAddress(address);
        result.setBytes(value);
        result.setUuid( uuid);
        dataDispatcher.onReceivedResult(result);
    }

    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS){
            return;
        }
        BleLog.d("CharacteristicGattCallback :: onCharacteristicWrite ");
        String address = gatt.getDevice().getAddress();
        byte[] value = characteristic.getValue();
        if (value != null){
            String setValue = ByteUtil.getHexString(value);
            BleLog.d("onCharacteristicWrite : "+ setValue);
            Result result = new Result(Constants.PROPERTY_WRITE);
            result.setUuid(characteristic.getUuid().toString());
            result.setAddress(address);
            result.setBytes(value);
            dataDispatcher.onReceivedResult(result);

        }
       else {
            BleLog.d("onCharacteristicWrite : value is null " );
        }
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        BleLog.d("CharacteristicGattCallback :: onCharacteristicChanged ");
        String address = gatt.getDevice().getAddress();
        byte[] value = characteristic.getValue();
        Result result = new Result(Constants.PROPERTY_NOTIFY);
        result.setAddress(address);
        result.setBytes(value);
        dataDispatcher.onReceivedResult(result);
    }

}