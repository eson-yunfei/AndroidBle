package org.eson.liteble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDexApplication;

import com.e.ble.BLESdk;
import com.shon.dispatcher.Dispatcher;
import com.shon.dispatcher.DispatcherConfig;

import org.eson.liteble.ble.command.BleTransmitter;
import org.eson.liteble.ble.command.Command;
import org.eson.liteble.ble.BleService;
import org.eson.liteble.share.ConfigShare;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/23 9:50
 * @change
 * @chang time
 * @class describe
 */
public class MyApplication extends MultiDexApplication {

    protected static Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static MyApplication instance;

    private String currentShowDevice = "";


    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        LittleBleViewModel.iniViewModel(this);
        BLESdk.get().init(mContext);
        BLESdk.get().setMaxConnect(3);
        ConfigShare configShare = new ConfigShare(mContext);
        BLESdk.get().setMaxConnect(configShare.getMaxConnect());
//		BLESdk.get().setPermitConnectMore(configShare.isPermitConnectMore());
        Intent bleServer = new Intent(mContext, BleService.class);
        startService(bleServer);

        DispatcherConfig dispatcherConfig = new DispatcherConfig.Builder()
                .setApiInterface(Command.class)
                .setTransmitter(BleTransmitter.getTransmitter())
                .build();
        Dispatcher.init(dispatcherConfig);

    }

    public String getCurrentShowDevice() {
        return currentShowDevice;
    }

    public void setCurrentShowDevice(String currentShowDevice) {
        this.currentShowDevice = currentShowDevice;
    }

}
