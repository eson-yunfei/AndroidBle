package org.eson.liteble.activity.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @package_name org.eson.liteble.activity.bean
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/4/4.
 * @description
 */

public class DescriptorBean implements Parcelable {
    private String UUID;
    private int permissions;

    public DescriptorBean() {
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
