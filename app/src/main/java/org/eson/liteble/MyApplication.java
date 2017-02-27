package org.eson.liteble;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.eson.ble_sdk.BLESdk;
import org.eson.liteble.service.BleService;
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
public class MyApplication extends Application {

	private static Context mContext;
	private static MyApplication instance;


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
		//initial BLE sdk
		BLESdk.init(mContext);

		Intent bleServer = new Intent(mContext, BleService.class);
		startService(bleServer);


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
		if (className.equals(currentName)) {
			return true;
		}
		return false;
	}

}
