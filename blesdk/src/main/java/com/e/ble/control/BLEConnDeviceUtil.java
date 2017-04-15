/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.e.ble.control;

import com.e.ble.bean.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @package_name com.e.ble.control
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/4/15.
 * @description
 */

class BLEConnDeviceUtil {

    private static volatile List<BLEConnBean> mBLEConnBeen;

    private static volatile BLEConnDeviceUtil sDeviceUtil;

    private BLEConnDeviceUtil() {
        if (mBLEConnBeen == null) {
            mBLEConnBeen = new ArrayList<>();
        }
    }

    public static BLEConnDeviceUtil get() {

        if (sDeviceUtil != null) {
            return sDeviceUtil;
        }

        synchronized (BLEConnDeviceUtil.class) {
            if (sDeviceUtil == null) {
                sDeviceUtil = new BLEConnDeviceUtil();
            }
            return sDeviceUtil;
        }
    }


    public void addConnectBean(String address) {

        BLEConnBean bean = getExistBean(address);
        if (bean == null) {
            bean = new BLEConnBean(address);
            synchronized (mBLEConnBeen) {
                mBLEConnBeen.add(bean);
            }
            return;
        }
        bean.setStartConnTime(System.nanoTime());
    }

    public void removeConnBean(String address) {
        BLEConnBean bean = getExistBean(address);
        if (bean == null) {
            return;
        }
        synchronized (mBLEConnBeen) {
            mBLEConnBeen.remove(bean);
        }
    }

    public BLEConnBean getExistBean(String address) {

        synchronized (mBLEConnBeen) {

            if (mBLEConnBeen.size() == 0) {
                return null;
            }
            for (int i = 0; i < mBLEConnBeen.size(); i++) {
                BLEConnBean bean = mBLEConnBeen.get(i);
                if (address.equals(bean.getAddress())) {
                    return bean;
                }
            }
            return null;
        }

    }
}
