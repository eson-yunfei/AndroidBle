package com.e.tool.ble.control;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.e.tool.ble.bean.ConnectBt;
import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.ble.util.BLELog;
import com.e.tool.ble.imp.OnStateChanged;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 15:15
 * Package name : com.e.ble.core.impl
 * Des : 设备 状态处理
 */
public class StateChangedImpl {

    private Handler handler;
    private final List<ConnectBean> connectBeanList = new ArrayList<>();
    private OnStateChanged onStateChangeListener;



    public StateChangedImpl() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public StateChangedImpl(ConnectBean connectBean) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        addConnectBean(connectBean);
    }

    public void setOnStateChangeListener(OnStateChanged listener) {
        this.onStateChangeListener = listener;
    }


    public boolean addConnectBean(ConnectBean connectBean) {

        synchronized (connectBeanList) {
            boolean isContainsBean = false;
            for (ConnectBean bean : connectBeanList) {
                if (TextUtils.equals(bean.getAddress(), connectBean.getAddress())) {
                    isContainsBean = true;
                    break;
                }
            }

            if (isContainsBean) {
                //存在，即添加设备，
                return false;
            }
            //不存在，添加成功
            connectBeanList.add(connectBean);
            return true;
        }
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
        final ConnectBean connectBean = getConnectBean(gatt);
        if (connectBean == null) {
            return;
        }
        final OnDevConnectListener onDevConnectListener = connectBean.getConnListener();
        if (onDevConnectListener == null) {
            return;
        }
        handler.post(() -> {
            ConnectBt connectBt = new ConnectBt(connectBean.getAddress()
            ,gatt.getDevice().getName());
            onDevConnectListener.onServicesDiscovered(connectBt);
        });

        synchronized (connectBeanList) {
            connectBeanList.remove(connectBean);
        }

        BLELog.e("connectBeanList : " + connectBeanList.size());
    }

    public void onConnectionStateChange(BluetoothGatt gatt, final int status, final int newState) {

        if (onStateChangeListener != null) {

            updateSateChange(gatt, status, newState);
        }

        ConnectBean connectBean = getConnectBean(gatt);
        if (connectBean == null) {
            return;
        }
        final OnDevConnectListener onDevConnectListener = connectBean.getConnListener();
        if (onDevConnectListener == null) {
            return;
        }
        if (status == BluetoothGatt.GATT_SUCCESS &&
                newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        }
        handler.post(() -> onDevConnectListener.onConnectSate(status, newState));

    }

    private void updateSateChange(BluetoothGatt gatt, int status, int newState) {
        BluetoothDevice device = gatt.getDevice();
        if (device == null) {
            return;
        }
        final String address = device.getAddress();
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }
        handler.post(() -> onStateChangeListener.onSateChanged(address,newState));
//        switch (newState) {
//            case BluetoothProfile.STATE_CONNECTING:
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        onStateChangeListener.onConnecting(address);
//                    }
//                });
//
//                break;
//            case BluetoothProfile.STATE_CONNECTED:
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        onStateChangeListener.onConnected(address);
//                    }
//                });
//
//                break;
//            case BluetoothProfile.STATE_DISCONNECTING:
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        onStateChangeListener.onDisconnecting(address);
//                    }
//                });
//
//                break;
//            case BluetoothProfile.STATE_DISCONNECTED:
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        onStateChangeListener.onDisconnected(address);
//                    }
//                });
//
//                break;
//            default:
//                break;
//        }
    }


    private ConnectBean getConnectBean(BluetoothGatt gatt) {
        ConnectBean connectBean = null;
        synchronized (connectBeanList) {
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
        }
        return connectBean;
    }


//    private void updateSate(ConnectBean connectBean, int status, int newState) {
//        OnDevConnectListener connListener = connectBean.getConnListener();
//        if (connListener == null) {
//            return;
//        }
//        connListener.onConnectSate(status, newState);
//    }



}
