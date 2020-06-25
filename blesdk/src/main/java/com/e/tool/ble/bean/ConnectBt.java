package com.e.tool.ble.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 18:54
 * Package name : com.e.tool.ble.bean
 * Des :
 */
public class ConnectBt implements Parcelable {
    private String address;
    private String name;

    public ConnectBt(String address, String name){
        this.address = address;
        this.name = name;
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

    public void setAddress(String address) {
        this.address = address;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(name);
    }
}
