package org.eson.liteble.activity.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description： Service UUID
 */

public class ServiceBean  implements Parcelable {
    private String serviceUUID;
    private String serviceType;

    private List<CharacterBean> mUUIDBeen;

    public ServiceBean(){}
    protected ServiceBean(Parcel in) {
        serviceUUID = in.readString();
        serviceType = in.readString();
        mUUIDBeen = in.createTypedArrayList(CharacterBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceUUID);
        dest.writeString(serviceType);
        dest.writeTypedList(mUUIDBeen);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceBean> CREATOR = new Creator<ServiceBean>() {
        @Override
        public ServiceBean createFromParcel(Parcel in) {
            return new ServiceBean(in);
        }

        @Override
        public ServiceBean[] newArray(int size) {
            return new ServiceBean[size];
        }
    };

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }


    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<CharacterBean> getUUIDBeen() {
        return mUUIDBeen;
    }

    public void setUUIDBeen(List<CharacterBean> UUIDBeen) {
        mUUIDBeen = UUIDBeen;
    }
}
