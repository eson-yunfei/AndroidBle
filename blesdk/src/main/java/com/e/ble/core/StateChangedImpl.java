package com.e.ble.core;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.text.TextUtils;

import com.e.ble.core.imp.OnConnectListener;
import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnStateChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 15:15
 * Package name : com.e.ble.core.impl
 * Des :
 */
public class StateChangedImpl {

    private List<ConnectBean> connectBeanList;
    private OnStateChangeListener onStateChangeListener;


    public StateChangedImpl() {

    }

    public StateChangedImpl(ConnectBean connectBean) {
        connectBeanList = new ArrayList<>();
        connectBeanList.add(connectBean);
    }

    public void addConnectBean(ConnectBean connectBean) {
        connectBeanList.add(connectBean);
    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
        ConnectBean connectBean = getConnectBean(gatt);
        if (connectBean == null) {
            return;
        }
        OnConnectListener onConnectListener = connectBean.getConnListener();
        if (onConnectListener == null) {
            return;
        }
        onConnectListener.onServicesDiscovered(connectBean.getAddress());
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

        if (onStateChangeListener != null) {
            updateSateChange(gatt, status, newState);
        }

        ConnectBean connectBean = getConnectBean(gatt);
        if (connectBean == null) {
            return;
        }
        OnConnectListener onConnectListener = connectBean.getConnListener();
        if (onConnectListener == null) {
            return;
        }
        onConnectListener.onConnectSate(status, newState);
    }

    private void updateSateChange(BluetoothGatt gatt, int status, int newState) {
        BluetoothDevice device = gatt.getDevice();
        if (device == null) {
            return;
        }
        String address = device.getAddress();
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTING:
                onStateChangeListener.onConnecting(address);
                break;
            case BluetoothProfile.STATE_CONNECTED:
                onStateChangeListener.onConnected(address);
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                onStateChangeListener.onDisconnecting(address);
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                onStateChangeListener.onDisconnected(address);
                break;
            default:
                break;
        }
    }


    private ConnectBean getConnectBean(BluetoothGatt gatt) {
        ConnectBean connectBean = null;
        for (ConnectBean bean : connectBeanList) {
            BluetoothDevice device = gatt.getDevice();
            if (device == null) {
                continue;
            }

            String address = device.getAddress();
            if (TextUtils.equals(bean.getAddress(), address)) {
                connectBean = bean;
                break;
            }

        }
        return connectBean;
    }

    private void updateSate(ConnectBean connectBean, int status, int newState) {
        OnConnectListener connListener = connectBean.getConnListener();
        if (connListener == null) {
            return;
        }
        connListener.onConnectSate(status, newState);
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.onStateChangeListener = listener;
    }

}
