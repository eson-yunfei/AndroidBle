package org.eson.liteble;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDexApplication;

import android.text.TextUtils;

import com.e.ble.BLESdk;
import com.shon.dispatcher.Dispatcher;
import com.shon.dispatcher.DispatcherConfig;
import com.shon.dispatcher.TransCall;
import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.imp.OnCallback;

import org.eson.liteble.command.BleTransmitter;
import org.eson.liteble.command.Command;
import org.eson.liteble.service.BleService;
import org.eson.liteble.share.ConfigShare;
import org.eson.liteble.util.LogUtil;

import java.util.List;

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

    private ConfigShare configShare;

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
        configShare = new ConfigShare(mContext);
        BLESdk.get().setMaxConnect(configShare.getMaxConnect());
//		BLESdk.get().setPermitConnectMore(configShare.isPermitConnectMore());
        Intent bleServer = new Intent(mContext, BleService.class);
        startService(bleServer);

        DispatcherConfig dispatcherConfig = new DispatcherConfig.Builder()
                .setApiInterface(Command.class)
                .setTransmitter(BleTransmitter.getTransmitter())
                .build();
        Dispatcher.init(dispatcherConfig);

//        Command command = Dispatcher.getInstance().getApi();
//
//        TransCall<String> transCall = command.sendCmd("123456");
//        LogUtil.e("transCall : " + transCall);
//        if (transCall != null)
//
//            transCall.execute((s, message) -> {
//                LogUtil.e("s = " + s);
//                LogUtil.e("message = " + message.toString());
//            });
    }


    /**
     * 判断某个界面是否在前台
     * 需添加权限
     * <uses-permission android:name="android.permission.GET_TASKS"/>
     *
     * @param className 某个界面名称
     */
    public boolean isForeground(String className) {
        if (mContext == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list == null || list.size() == 0) {
            return false;

        }
        ComponentName cpn = list.get(0).topActivity;
        String currentName = cpn.getClassName();
        LogUtil.e("currentName--->>" + currentName);
        return className.equals(currentName);
    }


    public String getCurrentShowDevice() {
        return currentShowDevice;
    }

    public void setCurrentShowDevice(String currentShowDevice) {
        this.currentShowDevice = currentShowDevice;
    }

    public ConfigShare getConfigShare() {
        if (configShare == null) {
            configShare = new ConfigShare(mContext);
        }
        return configShare;
    }
}
