package org.eson.liteble.ble.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description： 服务里面的  Character
 */

public class CharacterBean implements Parcelable {
    private String characterUUID;
    private String serviceUUID;

    private boolean read = false;       // read
    private boolean write = false;      // write
    private boolean notify = false;     //  notify

    private boolean listening = false;      //  notify 是否为正在监听状态

    private List<DescriptorBean> descriptorBeen;

    public CharacterBean() {
    }


    protected CharacterBean(Parcel in) {
        characterUUID = in.readString();
        serviceUUID = in.readString();
        read = in.readByte() != 0;
        write = in.readByte() != 0;
        notify = in.readByte() != 0;
        listening = in.readByte() != 0;
        descriptorBeen = in.createTypedArrayList(DescriptorBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(characterUUID);
        dest.writeString(serviceUUID);
        dest.writeByte((byte) (read ? 1 : 0));
        dest.writeByte((byte) (write ? 1 : 0));
        dest.writeByte((byte) (notify ? 1 : 0));
        dest.writeByte((byte) (listening ? 1 : 0));
        dest.writeTypedList(descriptorBeen);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CharacterBean> CREATOR = new Creator<CharacterBean>() {
        @Override
        public CharacterBean createFromParcel(Parcel in) {
            return new CharacterBean(in);
        }

        @Override
        public CharacterBean[] newArray(int size) {
            return new CharacterBean[size];
        }
    };

    public String getCharacterUUID() {
        return characterUUID;
    }

    public void setCharacterUUID(String characterUUID) {
        this.characterUUID = characterUUID;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public List<DescriptorBean> getDescriptorBeen() {
        return descriptorBeen;
    }

    public void setDescriptorBeen(List<DescriptorBean> descriptorBeen) {
        this.descriptorBeen = descriptorBeen;
    }


}
