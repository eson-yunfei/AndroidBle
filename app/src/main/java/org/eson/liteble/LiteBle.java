package org.eson.liteble;

import androidx.multidex.MultiDexApplication;

import com.shon.bluetooth.BLEManager;

import org.eson.log.LogUtils;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/23 9:50
 *
 * @change  切换到新的框架
 * @chang 2020/12/06
 * @class describe
 */
public class LiteBle extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

//        LittleBleViewModel.iniViewModel(this);
        LogUtils.init();
        BLEManager.init(this);
    }


}
