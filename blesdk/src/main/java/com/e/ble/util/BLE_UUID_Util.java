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

package com.e.ble.util;

import android.text.TextUtils;

import java.util.UUID;

/**
 * @package_name org.eson.liteble.util
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/15.
 * @description
 */

public class BLE_UUID_Util {

    public static final String UUID_FORMAT = "0000%04x-0000-1000-8000-00805f9b34fb";


    public static final class GattService {
        public static final int Generic_Access = 0x1800;
        public static final int Generic_Attribute = 0x1801;
        public static final int Device_Information = 0x180A;
    }


    public static final class GattCharacteristics {
        public static final int Device_Name = 0x2A00;
        public static final int Appearance = 0x2A01;
        public static final int Peripheral_Preferred_Connection_Parameters = 0x2A04;
        public static final int Service_Changed = 0x2A05;

        public static final int Firmware_Revision_String = 0x2A26;
        public static final int Hardware_Revision_String = 0x2A27;
        public static final int Manufacturer_Name_String = 0x2A29;
    }


    public static String getServiceNameByUUID(UUID uuid) {
        int serviceID = getValue(uuid);
        UUID uuid1 = makeUUID(serviceID);

        if (!TextUtils.equals(uuid.toString(), uuid1.toString())) {
            return "Unknown Service";
        }
        switch (serviceID) {
            case GattService.Generic_Access:
                return "Generic Access";
            case GattService.Generic_Attribute:
                return "Generic Attribute";
            case GattService.Device_Information:
                return "Device Information";
            default:
                return "Unknown Service";
        }
    }


    public static String getCharacterNameByUUID(UUID characterUUID) {
        int characterID = getValue(characterUUID);
        UUID uuid1 = makeUUID(characterID);

        if (!TextUtils.equals(characterUUID.toString(), uuid1.toString())) {
            return "Unknown character";
        }

        switch (characterID) {
            case GattCharacteristics.Device_Name:
                return "Device Name";
            case GattCharacteristics.Appearance:
                return "Appearance";
            case GattCharacteristics.Peripheral_Preferred_Connection_Parameters:
                return "Peripheral Preferred Connection Parameters";
            case GattCharacteristics.Service_Changed:
                return "Service Changed";
            case GattCharacteristics.Firmware_Revision_String:
                return "Firmware Revision String";
            case GattCharacteristics.Hardware_Revision_String:
                return "Hardware Revision String";
            case GattCharacteristics.Manufacturer_Name_String:
                return "Manufacturer Name String";
            default:
                return "Unknown character";
        }
    }


    public static UUID makeUUID(int value) {
        return UUID.fromString(String.format(UUID_FORMAT, value));
    }

    public static String getHexValue(UUID uuid) {

        return Integer.toHexString(getValue(uuid));
    }

    public static int getValue(UUID uuid) {
        return (int) (uuid.getMostSignificantBits() >>> 32);

    }
}
