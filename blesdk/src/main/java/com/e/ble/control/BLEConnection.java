///*
// * Copyright (c) 2017. xiaoyunfei
// *
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *     you may not use this file except in compliance with the License.
// *     You may obtain a copy of the License at
// *
// *         http://www.apache.org/licenses/LICENSE-2.0
// *
// *     Unless required by applicable law or agreed to in writing, software
// *     distributed under the License is distributed on an "AS IS" BASIS,
// *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *     See the License for the specific language governing permissions and
// *     limitations under the License.
// */
//
//package com.e.ble.control;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.content.Context;
//import android.os.Handler;
//import androidx.annotation.NonNull;
//import android.text.TextUtils;
//
//import com.e.ble.BLESdk;
//import com.e.ble.bean.BLEConnBean;
//import com.e.ble.check.BLECheck;
//import com.e.ble.control.listener.BLEConnListener;
//import com.e.ble.control.listener.BLEStateChangeListener;
//import com.e.ble.util.BLEError;
//import com.e.ble.util.BLELog;
//
///**
// * @作者 xiaoyunfei
// * @日期: 2017/3/5
// * @说明： 设备 连接 & 断开 控制类;
// * <p>
// * --     包括设备的状态监听
// */
//class BLEConnection implements BLEConnListener, BLEStateChangeListener {
//
//    private static volatile BLEConnection bleConnection = null;
//
//    private BluetoothAdapter bluetoothAdapter = null;
//
//    //回调接口
//    private BLEConnListener bleConnectionListener = null;
//    private BLEStateChangeListener stateChangeListener = null;
//
//
//    private Handler mHandler;
//
//
//    private BLEConnection() {
//        bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
//        mHandler = new Handler();
//    }
//
//    public static BLEConnection get() {
//        if (bleConnection != null) {
//            return bleConnection;
//
//        }
//        synchronized (BLEConnection.class) {
//            if (bleConnection == null) {
//                bleConnection = new BLEConnection();
//            }
//        }
//        return bleConnection;
//    }
//
//
//    /**
//     * 连接到指定设备
//     *
//     * @param context
//     * @param address
//     * @param bleGattCallBack
//     */
//    public void connectToAddress(@NonNull final Context context,
//                                 @NonNull final String address,
//                                 final BluetoothGattCallback bleGattCallBack) {
//
//        if (!isCanConnect(address)) {
//            return;
//        }
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                connToDevice(address, context, bleGattCallBack);
//            }
//        });
//
//    }
//
//    /**
//     * 断开所以设备连接
//     * 多连设备时可用
//     */
//    public void disConnectAll() {
//        BLEConnList.get().cleanConnDevice();
//    }
//
//    /**
//     * 断开设备连接
//     * 连设备时可用
//     *
//     * @param deviceAddress
//     */
//    public void disConnect(String deviceAddress) {
//
//        BLEConnList.get().delConnDevice(deviceAddress);
//    }
//
//
//    private boolean isCanConnect(String address) {
//        if (TextUtils.isEmpty(address)) {
//            onConnError(address, 0);
//            return false;
//        }
//
//        if (!BLECheck.get().isBleEnable()) {
//            //蓝牙是否为打开
//            onConnError(address, BLEError.BLE_CLOSE);
//            return false;
//        }
//
//        BLEConnBean connBean = BLEConnList.get().getContainBean(address);
//        if (connBean != null) {
//            if (BLEUtil.isConnected(address)) {
//                onAlreadyConnected(address);
//                return false;
//            }
//            return true;
//        }
//        if (BLEConnList.get().isOutOfLimit()) {
//            //超出最多设置的连接数，返回超限，
//            onConnError(address, BLEError.BLE_OUT_MAX_CONNECT);
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * @param address
//     * @param context
//     * @param bleGattCallBack
//     */
//    private void connToDevice(@NonNull String address,
//                              @NonNull Context context,
//                              BluetoothGattCallback bleGattCallBack) {
//
//        BluetoothGatt gatt = null;
//
//        BLEConnBean connBean = BLEConnList.get().getContainBean(address);
//        if (connBean != null) {
//            gatt = connBean.getBluetoothGatt();
//            boolean isGattConnect = BLEUtil.isConnected(address);
//            if (isGattConnect) {
//                onAlreadyConnected(address);
//                return;
//            }
//            if (gatt != null) {
//                gatt.connect();
//                return;
//            }
//        } else {
//            connBean = new BLEConnBean(address);
//        }
//
//        BLELog.i("create new bluetoothGatt");
//
//        final BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
//        if (bluetoothDevice == null) {
//            onConnError(address, 0);
//            return;
//        }
//        gatt = bluetoothDevice.connectGatt(context, false, bleGattCallBack);
//        connBean.setBluetoothGatt(gatt);
//        BLEConnList.get().addConnDevice(connBean);
//
//        BLELog.i("create new bluetoothGatt  finish");
//    }
//
//	/*
//      |---------------------------------------------------------------------------------------------------------|
//	  <p>
//	  | BLEConnection 提供给外部的方法，设置回调接口
//	  <p>
//	  |--------------------------------------------------------------------------------------------------------|
//	 */
//
//    /***
//     * 设置设备连接回调
//     * @param connectListener
//     */
//    public void setBleConnectListener(BLEConnListener connectListener) {
//        bleConnectionListener = connectListener;
//    }
//
//    /**
//     * 设置设备的状态监听
//     *
//     * @param stateChangedListener
//     */
//    public void setBleStateChangedListener(BLEStateChangeListener stateChangedListener) {
//        this.stateChangeListener = stateChangedListener;
//    }
//
//
//    /**
//     * |---------------------------------------------------------------------------------------------------------|
//     * <p>
//     * | BLEConnListener  的接口实现
//     * <p>
//     * |--------------------------------------------------------------------------------------------------------|
//     */
//
//    @Override
//    public void onAlreadyConnected(String address) {
//        if (bleConnectionListener != null) {
//            bleConnectionListener.onAlreadyConnected(address);
//        }
//    }
//
//
//    @Override
//    public void onConnError(String address, int errorCode) {
//        if (bleConnectionListener != null) {
//            bleConnectionListener.onConnError(address, errorCode);
//        }
//    }
//
//    @Override
//    public void onConnSuccess(String address) {
//        if (bleConnectionListener != null) {
//            bleConnectionListener.onConnSuccess(address);
//        }
//    }
//
//    /**
//     * |---------------------------------------------------------------------------------------------------------|
//     * <p>
//     * | BLEStateChangeListener  的接口实现
//     * <p>
//     * |--------------------------------------------------------------------------------------------------------|
//     */
//    @Override
//    public void onStateConnected(String address) {
//
//        if (stateChangeListener != null) {
//            stateChangeListener.onStateConnected(address);
//        }
//    }
//
//    @Override
//    public void onStateConnecting(String address) {
//        if (stateChangeListener != null) {
//            stateChangeListener.onStateConnecting(address);
//        }
//    }
//
//    @Override
//    public void onStateDisConnecting(String address) {
//        if (stateChangeListener != null) {
//            stateChangeListener.onStateDisConnecting(address);
//        }
//    }
//
//    @Override
//    public void onStateDisConnected(String address) {
//        if (stateChangeListener != null) {
//            stateChangeListener.onStateDisConnected(address);
//        }
//    }
//
//}
