package com.e.ble.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.text.TextUtils;

import com.e.ble.core.bean.ReadMessage;
import com.e.ble.core.imp.OnConnectListener;
import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnStateChangeListener;
import com.e.ble.util.BLELog;

import java.util.List;
import java.util.UUID;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:30
 * Package name : com.e.ble.control
 * Des :
 */
public class Controller {
    private BleTool bleTool;
    private Connector connector;

    Controller(BleTool bleTool) {
        this.bleTool = bleTool;
    }


    /**
     * 连接到指定 的蓝牙设备
     *
     * @param address
     * @param bleConnListener
     */
    public void connectDevice(String address, OnConnectListener bleConnListener) {

        ConnectBean connectBean = new ConnectBean(address, bleConnListener);

        if (connector == null) {
            connector = new Connector(bleTool.getContext(), bleTool.getBluetoothAdapter());
            connector.start();
        }
        connector.addConnect(connectBean);
    }

    public void setSateChangeListener(OnStateChangeListener onStateChangeListener) {
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (gattCallBack != null) {
            gattCallBack.setOnStateChangeListener(onStateChangeListener);
        }
    }

    public void readInfo(ReadMessage readMessage, OnReadMessage onReadMessage) {
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (gattCallBack != null) {
            gattCallBack.setReadListener(onReadMessage);
        }

        BluetoothGatt bluetoothGatt = getGatt(readMessage.getAddress());

        if (bluetoothGatt == null){
            onReadMessage.onReadError();
            return;
        }
        UUID serviceUuid = readMessage.getServiceUUID();
        UUID characteristicUuid = readMessage.getCharacteristicUUID();

        BluetoothGattCharacteristic characteristic =
                getCharacteristicByUUID(bluetoothGatt,
                        serviceUuid,
                        characteristicUuid);
        if (characteristic == null) {
            onReadMessage.onReadError();
            return;
        }
        if (!bluetoothGatt.readCharacteristic(characteristic)) {
            onReadMessage.onReadError();
        }

    }

    /**
     * 获取指定的 GattCharacteristic
     *
     * @param serviceUuid
     * @param characteristicUuid
     * @return
     */
    private BluetoothGattCharacteristic getCharacteristicByUUID(
            BluetoothGatt bluetoothGatt,
            UUID serviceUuid,
            UUID characteristicUuid) {
        BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(characteristicUuid);
    }

    public BluetoothGatt getGatt(String address) {
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        BluetoothGatt gatt = gattCallBack.getBluetoothGatt();
        if (gatt == null){
            return null;
        }

        BluetoothAdapter adapter = bleTool.getBluetoothAdapter();

        BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(address);

        int state = bleTool.getBluetoothManager().getConnectionState(bluetoothDevice,BluetoothProfile.GATT);

        BLELog.e("state == " + state);
        if (state == BluetoothProfile.STATE_CONNECTED){
            BLELog.e("address  :  " + address + " ; 已连接");
            return gatt;
        }

        return null;
    }
}
