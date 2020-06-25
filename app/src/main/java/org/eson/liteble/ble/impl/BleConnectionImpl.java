//package org.eson.liteble.ble.impl;
//
//import com.e.ble.control.BLEControl;
//import com.e.ble.control.listener.BLEConnListener;
//import com.e.ble.util.BLEConstant;
//
//import org.eson.liteble.util.LogUtil;
//
///**
// * Auth : xiao_yun_fei
// * Date : 2020/6/22 22:52
// * Package name : org.eson.liteble.ble.impl
// * Des :
// */
//public class BleConnectionImpl extends StateChanged implements BLEConnListener {
//    @Override
//    public void onConnError(String address, int errorCode) {
//        LogUtil.e("address -->>" + address + "; errorCode -->>" + errorCode);
//        if (errorCode == 133) {
//            BLEControl.get().disconnect(address);
//        }
//        updateBleState(address, BLEConstant.Connection.STATE_CONNECT_FAILED);
//    }
//
//    @Override
//    public void onConnSuccess(String address) {
//
//        updateBleState(address, BLEConstant.Connection.STATE_CONNECT_SUCCEED);
//
//    }
//
//    @Override
//    public void onAlreadyConnected(String address) {
//        updateBleState(address, BLEConstant.Connection.STATE_CONNECT_CONNECTED);
//    }
//}
