package org.eson.liteble.detail.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 15:28
 * Package name : org.eson.liteble.detail.bean
 * Des :
 */
public class DescriptorBean implements Parcelable {
    private String UUID;
    private int permissions;

    public DescriptorBean(){

    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public static Creator<DescriptorBean> getCREATOR() {
        return CREATOR;
    }

    protected DescriptorBean(Parcel in) {
        UUID = in.readString();
        permissions = in.readInt();
    }

    public static final Creator<DescriptorBean> CREATOR = new Creator<DescriptorBean>() {
        @Override
        public DescriptorBean createFromParcel(Parcel in) {
            return new DescriptorBean(in);
        }

        @Override
        public DescriptorBean[] newArray(int size) {
            return new DescriptorBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UUID);
        dest.writeInt(permissions);
    }
}
