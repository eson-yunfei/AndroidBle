package org.eson.liteble.common.share

import android.content.Context
import org.eson.liteble.LiteBle


class ConfigPreferences<T> constructor(context: Context, keyName: String, value: T) : Preferences<T>(context, keyName, value) {


    companion object {


        // 是否支持设备多联
        @JvmStatic
        var connectMore by ConfigPreferences(LiteBle.context, "permit_connect_more", false)

        //搜索时长
        @JvmStatic
        var scannerTime :Int by ConfigPreferences(LiteBle.context, "scanner_time", 5000)

        //最大连接数
        @JvmStatic
        var maxConnect by ConfigPreferences(LiteBle.context, "max_connect", 1)

        //是否过滤无名称设备
        @JvmStatic
        var filterNoName by ConfigPreferences(LiteBle.context, "filter_empty_name", false)
    }

    override fun getShareName(): String = "lite_ble_config"

}