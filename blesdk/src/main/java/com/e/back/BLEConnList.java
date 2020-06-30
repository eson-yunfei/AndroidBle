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
//import android.text.TextUtils;
//
//import com.e.ble.BLESdk;
//import com.e.ble.bean.BLEConnBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @package_name com.e.ble.control
// * @name ${name}
// * <p>
// * Created by xiaoyunfei on 2017/4/21.
// * @description
// */
//
//public class BLEConnList {
//    private static volatile BLEConnList sConnList = null;
//    private static volatile List<BLEConnBean> sConnBeanList = null;
//
//    private BLEConnList() {
//
//        if (sConnBeanList == null) {
//            sConnBeanList = new ArrayList<>();
//        }
//    }
//
//    public static BLEConnList get() {
//
//        if (sConnList != null) {
//            return sConnList;
//        }
//        synchronized (BLEConnList.class) {
//            if (sConnList == null) {
//                sConnList = new BLEConnList();
//            }
//            return sConnList;
//        }
//    }
//
//
//    public boolean isOutOfLimit() {
//        synchronized (sConnBeanList) {
//            return sConnBeanList.size() >= BLESdk.get().getMaxConnect();
//        }
//    }
//
//
//    /**
//     * @param connBean
//     */
//    public void addConnDevice(BLEConnBean connBean) {
//
//        BLEConnBean bleConnBean = getContainBean(connBean.getAddress());
//        if (bleConnBean == null) {
//            addDevice(connBean);
//            return;
//        }
////        bleConnBean.setStartConnTime(0);
//        bleConnBean.setBluetoothGatt(connBean.getBluetoothGatt());
//    }
//
//
//    /**
//     * @param address
//     */
//    public void delConnDevice(String address) {
//        BLEConnBean bleConnBean = getContainBean(address);
//        if (bleConnBean == null) {
//            return;
//        }
//        BluetoothGatt gatt = bleConnBean.getBluetoothGatt();
//        BLEUtil.disConnect(address, gatt);
//        synchronized (sConnBeanList) {
//            sConnBeanList.remove(bleConnBean);
//        }
//    }
//
//    public void cleanConnDevice() {
//        synchronized (sConnBeanList) {
//            while (sConnBeanList.size() > 0) {
//                BLEConnBean connBean = sConnBeanList.get(0);
//                delConnDevice(connBean.getAddress());
//            }
//        }
//    }
//
//    private void addDevice(BLEConnBean connBean) {
//        synchronized (sConnBeanList) {
//            sConnBeanList.add(connBean);
//        }
//    }
//
//    public BLEConnBean getContainBean(String address) {
//        synchronized (sConnList) {
//            for (BLEConnBean bleConnBean : sConnBeanList) {
//                if (TextUtils.equals(address,
//                        bleConnBean.getAddress())) {
//
//                    return bleConnBean;
//                }
//            }
//        }
//        return null;
//    }
//}
