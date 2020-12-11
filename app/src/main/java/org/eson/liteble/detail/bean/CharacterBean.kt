package org.eson.liteble.detail.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description： 服务里面的  Character
 */
data class CharacterBean (
        val address: String?,
        val name: String?,
        val characterUUID: String?,
        val serviceUUID: String?,
        var read: Boolean = false,// read
        var write: Boolean = false, // write
        var notify: Boolean = false,//  notify
        var listening: Boolean = false,//  notify 是否为正在监听状态
        var descriptorBeen: List<DescriptorBean>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArrayList(DescriptorBean.CREATOR)) {
    }


    companion object CREATOR : Parcelable.Creator<CharacterBean> {
        override fun createFromParcel(parcel: Parcel): CharacterBean {
            return CharacterBean(parcel)
        }

        override fun newArray(size: Int): Array<CharacterBean?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(name)
        parcel.writeString(characterUUID)
        parcel.writeString(serviceUUID)
        parcel.writeByte(if (read) 1 else 0)
        parcel.writeByte(if (write) 1 else 0)
        parcel.writeByte(if (notify) 1 else 0)
        parcel.writeByte(if (listening) 1 else 0)
        parcel.writeTypedList(descriptorBeen)
    }

    override fun describeContents(): Int {
        return 0
    }
}
