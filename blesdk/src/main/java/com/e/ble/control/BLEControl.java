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
//import android.bluetooth.BluetoothGatt;
//import android.content.Context;
//
//import com.e.ble.bean.BLEUuid;
//import com.e.ble.control.listener.BLEConnListener;
//import com.e.ble.control.listener.BLEReadRssiListener;
//import com.e.ble.control.listener.BLEStateChangeListener;
//import com.e.ble.control.listener.BLETransportListener;
//import com.e.ble.receiver.BLEStateReceiver;
//import com.e.ble.receiver.listener.BLEReceiverListener;
//import com.e.ble.scan.BLEScanner;
//
///**
// * |---------------------------------------------------------------------------------------------------------------|
// *
// * @作者 xiaoyunfei
// * @日期: 2017/3/5
// * @说明： 蓝牙控制类，包括设备的连接、断开、数据发送，回调设置
// * <p>
// * |---------------------------------------------------------------------------------------------------------------|
// */
//
//public class BLEControl {
//
//    private static volatile BLEControl bleControl = null;
//    private static volatile BLEGattCallBack sGattCallBack;
//    private BLEReadRssiListener bleReadRssiListener;
//
//    private final ThreadLocal<BLEReceiverListener> mBLEReceiverListener = new ThreadLocal<>();
//
//    private BLEControl() {
//        if (sGattCallBack == null) {
//            sGattCallBack = new BLEGattCallBack();
//        }
//    }
//
//    public static void init() {
//        if (bleControl == null) {
//            bleControl = new BLEControl();
//        }
//    }
//
//
//    public static BLEControl get() {
//        if (bleControl != null) {
//            return bleControl;
//        }
//        synchronized (BLEControl.class) {
//            init();
//        }
//        return bleControl;
//    }
//
//
//    /**
//     * 连接到指定的设备
//     *
//     * @param context
//     * @param device
//     */
//    public void connectDevice(Context context, String device) {
//        BLEScanner.get().stopScan();    //连接设备前停止扫描设备
//        BLEConnection.get().connectToAddress(context, device, sGattCallBack);
//    }
//
//
//    /**
//     * 判断一个设备是否为连接状态
//     *
//     * @param deviceAddress
//     * @return
//     */
//    public boolean isConnect(String deviceAddress) {
//
//        return BLEUtil.isConnected(deviceAddress);
//    }
//
//
//    /**
//     * 断开所以的设备连接
//     * 多连时可用
//     */
//    public void disconnectAll() {
//        BLEConnection.get().disConnectAll();
//    }
//
//    /**
//     * 断开指定设备的连接
//     *
//     * @param deviceAddress
//     */
//    public void disconnect(String deviceAddress) {
//        BLEConnection.get().disConnect(deviceAddress);
//    }
//
//
//    /**
//     * 发送数据
//     *
//     * @param bleUuid
//     */
//    public boolean sendData(BLEUuid bleUuid) {
//
//        return BLETransport.get().sendDataToDevice(bleUuid);
//    }
//
//    /**
//     * 启用通知
//     *
//     * @param bleUuid
//     */
//    public void enableNotify(BLEUuid bleUuid) {
//        BLETransport.get().enableNotify(bleUuid);
//    }
//
//    /**
//     * 读取数据
//     *
//     * @param bleUuid
//     */
//    public boolean readDeviceData(BLEUuid bleUuid) {
//        return BLETransport.get().readDeviceData(bleUuid);
//    }
//
//
//	/*
//      |------------------------------------------------------------------------------------------------------|
//	  <p>
//	  |  BLEControl 提供给外部访问的 API   读取设备的 Rssi 信号值
//	  <p>
//	  |-----------------------------------------------------------------------------------------------------|
//	 */
//
//    /**
//     * 读取设备的 Rssi
//     *
//     * @param deviceAddress
//     */
//    public void readGattRssi(String deviceAddress) {
//        BluetoothGatt gatt = BLEUtil.getBluetoothGatt(deviceAddress);
//        if (gatt == null) {
//            return;
//        }
//        gatt.readRemoteRssi();
//
//
//    }
//
//    public void onReadRssi(String address, int rssi) {
//
//        if (bleReadRssiListener == null) {
//            return;
//        }
//        bleReadRssiListener.onReadRssi(address, rssi);
//    }
//
//    //    @Override
//    public void onReadRssiError(String address, int errorCode) {
//        if (bleReadRssiListener == null) {
//            return;
//        }
//        bleReadRssiListener.onReadRssiError(address, errorCode);
//    }
//
//
//    public BluetoothGatt getBluetoothGatt(String connectMac) {
//
//        return BLEUtil.getBluetoothGatt(connectMac);
//    }
//
//	/*
//      |------------------------------------------------------------------------------------------------------|
//	  <p>
//	  |  BLEControl 提供给外部访问的 API   设备回调相关
//	  <p>
//	  |-----------------------------------------------------------------------------------------------------|
//	 */
//
//    /**
//     * 设备发送数据，接收数据的回调
//     *
//     * @param bleTransportListener
//     */
//    public void setBleTransportListener(BLETransportListener bleTransportListener) {
//        BLETransport.get().setBleTransportListener(bleTransportListener);
//    }
//
//    /**
//     * 设备连接的回调
//     *
//     * @param connectListener
//     */
//    public void setBleConnectListener(BLEConnListener connectListener) {
//        BLEConnection.get().setBleConnectListener(connectListener);
//    }
//
//    /**
//     * 设备状态回调
//     *
//     * @param stateChangedListener
//     */
//    public void setBleStateChangedListener(BLEStateChangeListener stateChangedListener) {
//
//        BLEConnection.get().setBleStateChangedListener(stateChangedListener);
//    }
//
//    /**
//     * @param readRssiListener
//     */
//    public void setBleReadRssiListener(BLEReadRssiListener readRssiListener) {
//        bleReadRssiListener = readRssiListener;
//    }
//
//    public void setBLEReceiverListener(BLEReceiverListener bleReceiverListener) {
//
//        mBLEReceiverListener.set(bleReceiverListener);
//        if (mBLEReceiverListener.get() == null) {
//            mBLEReceiverListener.set(getBLEReceiverListener());
//        }
//        BLEStateReceiver.setBLEReceiverListener(mBLEReceiverListener.get());
//    }
//
//    private BLEReceiverListener getBLEReceiverListener() {
//        return new BLEReceiverListener() {
//            @Override
//            public void onStateOff() {
//                disconnectAll();
//            }
//
//            @Override
//            public void onStateOn() {
//
//            }
//
//            @Override
//            public void onStateStartOff() {
//
//            }
//
//            @Override
//            public void onStateStartOn() {
//
//            }
//        };
//    }
//
//
//}
