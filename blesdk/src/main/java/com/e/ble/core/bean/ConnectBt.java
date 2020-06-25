package com.e.ble.core.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 14:04
 * Package name : com.e.ble.core.bean
 * Des :
 */
public class ConnectBt implements Parcelable {


    private String address;
    private String name;

    public ConnectBt(String address) {
        this.address = address;
    }


    protected ConnectBt(Parcel in) {
        address = in.readString();
        name = in.readString();
    }

    public static final Creator<ConnectBt> CREATOR = new Creator<ConnectBt>() {
        @Override
        public ConnectBt createFromParcel(Parcel in) {
            return new ConnectBt(in);
        }

        @Override
        public ConnectBt[] newArray(int size) {
            return new ConnectBt[size];
        }
    };

    public String getAddress() {
        return address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(name);
    }
}
