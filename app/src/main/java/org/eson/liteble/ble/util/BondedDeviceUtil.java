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

package org.eson.liteble.ble.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package_name org.eson.liteble.util
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/5.
 * @description
 */

public class BondedDeviceUtil {

    private static BondedDeviceUtil sBondedDeviceUtil;
    private Map<String, BondedDeviceBean> mDeviceBeanMap;

    private BondedDeviceUtil() {
        if (mDeviceBeanMap == null) {
            mDeviceBeanMap = new HashMap<>();
        }
    }

    public static BondedDeviceUtil get() {
        if (sBondedDeviceUtil != null) {
            return sBondedDeviceUtil;
        }

        synchronized (BondedDeviceUtil.class) {
            if (sBondedDeviceUtil == null) {
                sBondedDeviceUtil = new BondedDeviceUtil();
            }
            return sBondedDeviceUtil;
        }
    }


    public void addBondDevice(String address) {

        if (isContainsDevice(address)) {
            return;
        }
        BondedDeviceBean bondedDeviceBean = new BondedDeviceBean(address);
        mDeviceBeanMap.put(address, bondedDeviceBean);
    }


    public void removeBondDevice(String address) {
        if (!isContainsDevice(address)) {
            return;
        }
        mDeviceBeanMap.remove(address);
    }

    public BondedDeviceBean getDevice(String address) {
        if (!isContainsDevice(address)) {
            return null;
        }
        return mDeviceBeanMap.get(address);
    }

    public boolean isContainsDevice(String address) {
        if (mDeviceBeanMap == null || mDeviceBeanMap.size() == 0) {
            return false;
        }
        return mDeviceBeanMap.containsKey(address);
    }

    public List<BondedDeviceBean> getBondedList() {
        List<BondedDeviceBean> bondedDeviceBeen = new ArrayList<>();
        if (mDeviceBeanMap == null || mDeviceBeanMap.size() == 0) {
            return bondedDeviceBeen;
        }
        for (Map.Entry<String, BondedDeviceBean> beanEntry : mDeviceBeanMap.entrySet()) {
            bondedDeviceBeen.add(beanEntry.getValue());
        }
        return bondedDeviceBeen;
    }
}
