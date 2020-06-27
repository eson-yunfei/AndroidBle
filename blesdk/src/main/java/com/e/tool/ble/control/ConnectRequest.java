package com.e.tool.ble.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.e.ble.util.BLELog;
import com.e.tool.ble.bean.ConnectResult;
import com.e.tool.ble.bean.DevState;
import com.e.tool.ble.control.gatt.BGattCallBack;
import com.e.tool.ble.control.request.Request;
import com.e.tool.ble.imp.OnDevConnectListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:43
 * Package name : com.e.ble.core
 * Des :
 */
class ConnectRequest extends Request {
    private Context context;
    private String address;
    private BluetoothAdapter bluetoothAdapter;
    private OnDevConnectListener connListener;
    private BluetoothGatt gatt;
    private BGattCallBack bGattCallBack;

    public ConnectRequest(Context context, String address,
                          BluetoothAdapter bluetoothAdapter,
                          BGattCallBack bGattCallBack,
                          OnDevConnectListener connListener) {
        this.context = context;
        this.address = address;
        this.bGattCallBack = bGattCallBack;
        this.bluetoothAdapter = bluetoothAdapter;
        this.connListener = connListener;
    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public BluetoothGatt getGatt() {
        return gatt;
    }

    public String getAddress() {
        return address;
    }

    public OnDevConnectListener getConnListener() {
        return connListener;
    }

    @Override
    public String toString() {
        return "ConnectBean{" +
                "address='" + address + '\'' +
                ", connListener=" + connListener +
                ", gatt=" + gatt +
                '}';
    }

    @Override
    public boolean launch() {

        BLELog.e("Connector -->> connect() ");
        if (bluetoothAdapter == null) {
            BLELog.e("Connector -->> connect() bluetoothAdapter is null  ");
            return false;
        }
        final BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(getAddress());
        if (bluetoothDevice == null) {
            BLELog.e("Connector -->> connect() bluetoothDevice is null  ");
            return false;
        }

        BluetoothGatt gatt = bluetoothDevice.connectGatt(context,
                false, bGattCallBack);
        if (gatt != null) {
            setGatt(gatt);
            return true;
        }
        return false;
    }

    public void onConnectError(ConnectResult connectResult) {
        isWaiting = false;

        if (connListener == null) {
            return;
        }
        connListener.onConnectError(connectResult);

    }

    public void onConnectSate(DevState devState) {
        if (connListener == null) {
            return;
        }
        connListener.onConnectSate(devState);
    }

    public void onServicesDiscovered(ConnectResult result) {
        isWaiting = false;
        if (connListener == null) {
            return;
        }
        connListener.onServicesDiscovered(result);
    }
}
