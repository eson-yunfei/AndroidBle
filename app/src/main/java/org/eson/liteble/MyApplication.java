package org.eson.liteble;

import android.annotation.SuppressLint;

import androidx.multidex.MultiDexApplication;

import com.e.ble.BLESdk;
import com.e.tool.ble.BleTool;
import com.shon.dispatcher.Dispatcher;
import com.shon.dispatcher.core.DispatcherConfig;

import org.eson.liteble.ble.command.BleTransmitter;
import org.eson.liteble.ble.command.Command;

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

    @Override
    public void onCreate() {
        super.onCreate();
        BleTool.getInstance().init(this);

        LittleBleViewModel.iniViewModel(this);

        BLESdk.get().init(this);

//        Intent bleServer = new Intent(mContext, BleService.class);
////        startService(bleServer);

        DispatcherConfig dispatcherConfig = new DispatcherConfig.Builder()
                .setApiInterface(Command.class)
                .setTransmitter(BleTransmitter.getTransmitter())
                .build();
        Dispatcher.init(dispatcherConfig);
    }


}
