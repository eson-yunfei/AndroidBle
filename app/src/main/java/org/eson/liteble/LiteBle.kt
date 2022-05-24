package org.eson.liteble

import androidx.multidex.MultiDexApplication
import com.shon.ble.BleManager
import org.eson.log.LogUtils

/**
 * @name AndroidBle
 * @class name：org.eson.liteble
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/23 9:50
 * @change 切换到新的框架
 * @chang 2020/12/06
 * @class describe
 */
class LiteBle : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()

        //初始化 log
        LogUtils.init()

        //初始化 Ble
        BleManager.initBleManager(this)
    }
}