package com.e.tool.ble.gatt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Looper;

import com.e.tool.ble.bean.state.ConnectError;
import com.e.tool.ble.bean.state.ConnectResult;
import com.e.tool.ble.bean.state.DevState;
import com.e.tool.ble.gatt.imp.StateChangeListener;
import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.tool.ble.imp.OnStateChanged;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 15:15
 * Package name : com.e.ble.core.impl
 * Des : 设备 状态处理
 */
class StateChangedImpl extends BaseImpl implements StateChangeListener {

    private OnStateChanged onStateChangeListener;
    private OnDevConnectListener onDevConnectListener;


    public StateChangedImpl() {
        super();
    }

    public void setOnStateChangeListener(OnStateChanged listener) {
        this.onStateChangeListener = listener;
    }

    @Override
    public void setConnectCallBack(OnDevConnectListener connectListener) {
        this.onDevConnectListener = connectListener;
    }


    /**
     * 设备服务 可以被找到
     *
     * @param gatt   gatt
     * @param status status
     */
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
        if (onDevConnectListener != null) {
            BluetoothDevice device = gatt.getDevice();
            ConnectResult connectBt = new ConnectResult(device.getAddress()
                    , device.getName(), status);
            connectBt.setServicesDiscovered(true);
            post(() -> onDevConnectListener.onServicesDiscovered(connectBt));

        }
//
    }

    /**
     * 设备状态改变
     *
     * @param gatt     gatt
     * @param status   status
     * @param newState newState
     */
    public void onConnectionStateChange(BluetoothGatt gatt, final int status, final int newState) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            gatt.disconnect();

            if (onDevConnectListener != null) {
                post(() -> postConnectError(gatt, status));
            }
            return;
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        }

        BluetoothDevice device = gatt.getDevice();
        DevState devState = new DevState(device.getAddress(),
                device.getName(), status);
        devState.setNewState(newState);
        if (onStateChangeListener != null) {
            //回复状态更改
            post(() -> onStateChangeListener.onSateChanged(devState));
        }

        if (onDevConnectListener != null) {
            onDevConnectListener.onConnectSate(devState);
        }
    }

    /**
     * @param gatt   gatt
     * @param status status
     */
    private void postConnectError(BluetoothGatt gatt, int status) {
        BluetoothDevice bluetoothDevice = gatt.getDevice();
        ConnectError connectError;
        if (bluetoothDevice == null) {
            return;
        }
        connectError = new ConnectError(bluetoothDevice.getAddress());
        connectError.setName(bluetoothDevice.getName());
        connectError.setStatus(status);
        onDevConnectListener.onConnectError(connectError);
    }


}
