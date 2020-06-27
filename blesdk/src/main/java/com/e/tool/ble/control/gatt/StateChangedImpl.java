package com.e.tool.ble.control.gatt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Looper;

import com.e.tool.ble.bean.ConnectResult;
import com.e.tool.ble.bean.DevState;
import com.e.tool.ble.control.gatt.imp.StateChangeListener;
import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.tool.ble.imp.OnStateChanged;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 15:15
 * Package name : com.e.ble.core.impl
 * Des : 设备 状态处理
 */
class StateChangedImpl implements StateChangeListener {

    private Handler handler;
    private OnStateChanged onStateChangeListener;
    private OnDevConnectListener onDevConnectListener;


    public StateChangedImpl() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
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
                    , device.getName(),status);
            connectBt.setServicesDiscovered(true);
            handler.post(() -> onDevConnectListener.onServicesDiscovered(connectBt));

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
                handler.post(() -> {
                    BluetoothDevice bluetoothDevice = gatt.getDevice();
                    ConnectResult connectResult;
                    if (bluetoothDevice != null) {
                        connectResult  = new ConnectResult(bluetoothDevice.getAddress(),
                                bluetoothDevice.getName(), status);
                    }else {
                        connectResult = new ConnectResult("","",status);
                    }
                    connectResult.setNewState(newState);
                    onDevConnectListener.onConnectError(connectResult);
                });
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
            handler.post(() -> onStateChangeListener.onSateChanged(devState));
        }

        if (onDevConnectListener != null){
            onDevConnectListener.onConnectSate(devState);
        }


    }


}
