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

package com.e.ble.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.e.ble.support.ScanRecord;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */
public class BLEDevice implements Parcelable {

    private String name;
    private String mac;
    private int rssi;
    private ScanRecord scanRecord;

    public BLEDevice() {

    }

    protected BLEDevice(Parcel in) {
        name = in.readString();
        mac = in.readString();
        rssi = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mac);
        dest.writeInt(rssi);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BLEDevice> CREATOR = new Creator<BLEDevice>() {
        @Override
        public BLEDevice createFromParcel(Parcel in) {
            return new BLEDevice(in);
        }

        @Override
        public BLEDevice[] newArray(int size) {
            return new BLEDevice[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


    public ScanRecord getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(ScanRecord scanRecord) {
        this.scanRecord = scanRecord;
    }

//    @Override
//    public boolean equals(Object obj) {
//        BLEDevice device;
//        if (obj instanceof BLEDevice) {
//            device = (BLEDevice) obj;
//            return device.mac.equals(this.mac);
//        } else {
//            return false;
//        }
//    }
}
