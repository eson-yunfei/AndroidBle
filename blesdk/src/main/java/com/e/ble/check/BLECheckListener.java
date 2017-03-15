package com.e.ble.check;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 蓝牙状态检测的结果
 */

public interface BLECheckListener {

	//没有蓝牙权限
	void noBluetoothPermission();

	//不支持蓝牙
	void notSupportBle();

	//蓝牙未打开
	void bleClosing();

	void bleStateOK();
}
