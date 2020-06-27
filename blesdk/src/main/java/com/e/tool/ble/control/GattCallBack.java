package com.e.tool.ble.control;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.text.TextUtils;

import com.e.ble.util.BLELog;
import com.e.tool.ble.bean.message.NotifyState;
import com.e.tool.ble.control.gatt.BGattCallBack;
import com.e.tool.ble.imp.OnDataNotify;
import com.e.tool.ble.imp.OnRead;
import com.e.tool.ble.imp.OnStateChanged;
import com.e.tool.ble.imp.OnWriteDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 13:53
 * Package name : com.e.ble.core
 * Des :
 */
final class GattCallBack extends BGattCallBack {

    private List<BluetoothGatt> bluetoothGattList;

    public GattCallBack() {
        super();
        bluetoothGattList = new ArrayList<>();
    }

    public BluetoothGatt getBluetoothGatt(String address) {
        for (BluetoothGatt bluetoothGatt : bluetoothGattList) {
            BluetoothDevice saveDev = bluetoothGatt.getDevice();
            String saveAddress = saveDev.getAddress();
            if (TextUtils.equals(address, saveAddress)) {
                return bluetoothGatt;
            }
        }
        return null;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        updateBluetoothGatt(gatt);
    }

    protected void updateBluetoothGatt(BluetoothGatt gatt) {
        BLELog.e("GattCallBack-->> updateBluetoothGatt ");
        final BluetoothDevice device = gatt.getDevice();
        final String address = device.getAddress();
        boolean canAdd = true;
        for (int i = 0; i < bluetoothGattList.size(); i++) {
            BluetoothGatt bluetoothGatt = bluetoothGattList.get(i);
            BluetoothDevice saveDev = bluetoothGatt.getDevice();
            String saveAddress = saveDev.getAddress();
            if (TextUtils.equals(address, saveAddress)) {
                canAdd = false;
                bluetoothGattList.set(i,gatt);
                break;
            }
        }

        if (canAdd) {
            bluetoothGattList.add(gatt);
        }
    }



}
