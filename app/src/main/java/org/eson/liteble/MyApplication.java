package org.eson.liteble;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import org.eson.ble_sdk.BLESdk;
import org.eson.liteble.service.BleService;

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
public class MyApplication extends Application {

	private static Context mContext;
	private static MyApplication instance;


	public MyApplication getInstance() {
//        if(instance==null){
//            synchronized (MyApplication.class){
//                if (instance==null){
//                    instance = new MyApplication();
//                }
//            }
//        }
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
		//initial BLE sdk
		BLESdk.init(mContext);

		Intent bleServer = new Intent(mContext, BleService.class);
		startService(bleServer);


	}
}
