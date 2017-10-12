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

import java.util.UUID;

/**
 * @package_name com.e.ble.scan
 * @name ${BLEScanCfg}
 * <p>
 * Created by xiaoyunfei on 2017/4/13.
 * @description
 */
public class BLEScanCfg {
    //999999999
    private int scanTime = -1;
    private String[] nameFilter = null;
    private UUID[] uuidFilter = null;

    public int getScanTime() {
        return scanTime;
    }

    public void setScanTime(int scanTime) {
        this.scanTime = scanTime;
    }

    public String[] getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String[] nameFilter) {
        this.nameFilter = nameFilter;
    }

    public UUID[] getUuidFilter() {
        return uuidFilter;
    }

    public void setUuidFilter(UUID[] uuidFilter) {
        this.uuidFilter = uuidFilter;
    }

    public static class ScanCfgBuilder {
        private BLEScanCfg mBLEScanCfg = new BLEScanCfg();

        public ScanCfgBuilder(int scanTime) {
            mBLEScanCfg.setScanTime(scanTime);
        }

        public ScanCfgBuilder addNameFilter(String... names) {

            if (names == null || names.length == 0) {
                return this;
            }
            String[] nameFilter = names.clone();
            mBLEScanCfg.setNameFilter(nameFilter);

            return this;
        }

        public ScanCfgBuilder addUUIDFilter(UUID... uuids) {
            if (uuids == null || uuids.length == 0) {
                return this;
            }
            UUID[] uuidFilter = uuids.clone();
            mBLEScanCfg.setUuidFilter(uuidFilter);
            return this;
        }

        public BLEScanCfg builder() {
            return mBLEScanCfg;
        }

    }
}
