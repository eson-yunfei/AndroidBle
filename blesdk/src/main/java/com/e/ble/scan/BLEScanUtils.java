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

package com.e.ble.scan;

import android.text.TextUtils;

import com.e.ble.scan.appcompat.ScanRecord;

/**
 * @package_name com.e.ble.scan
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/10/12.
 * @description
 */

class BLEScanUtils {

    /**
     * 获取设备名称
     *
     * @param name
     * @param scanRecord
     * @return
     */
    public static String getDeviceName(String name, ScanRecord scanRecord) {
        if (!TextUtils.isEmpty(name)) {
            return name;
        }
        if (scanRecord == null) {
            return name;
        }
        name = scanRecord.getDeviceName();
        if (!TextUtils.isEmpty(name)) {
            return name;
        }
        return "< UnKnow >";
    }

    /**
     * 检测设备名称是否在过滤名称范围内
     *
     * @param name
     * @param nameFilter
     * @return 如果包含设备名称，返回true
     */
    public static boolean isFilterContainName(String name, String[] nameFilter) {
        if (nameFilter == null || nameFilter.length == 0) {
            return true;
        }
        boolean isContain = false;
        for (String aNameFilter : nameFilter) {
            if (name.equals(aNameFilter)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }
}
