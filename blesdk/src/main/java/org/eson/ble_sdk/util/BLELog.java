package org.eson.ble_sdk.util;

import android.util.Log;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 日志输出工具
 */

public class BLELog {
	private BLELog() {

	}

	// 标签
	private static String TAG = "BLELog";

	//是否显示详细信息
	private static boolean needInfo = false;

	public static void setTAG(String TAG) {
		BLELog.TAG = TAG;
	}

	public static void setNeedInfo(boolean needInfo) {
		BLELog.needInfo = needInfo;
	}

	/**
	 * @param msg
	 */
	public static void e(String msg) {
		Log.e(TAG, getFormatString(msg) + getCurrentInfo());
	}

	/**
	 * @param msg
	 */
	public static void d(String msg) {
		Log.d(TAG, getFormatString(msg) + getCurrentInfo());
	}

	/**
	 * @param msg
	 */
	public static void i(String msg) {
		Log.i(TAG, getFormatString(msg) + getCurrentInfo());
	}

	/**
	 * @param msg
	 */
	public static void w(String msg) {
		Log.w(TAG, getFormatString(msg) + getCurrentInfo());
	}

	/**
	 * @param msg
	 *
	 * @return
	 */
	private static String getFormatString(String msg) {
		return "{ " + msg + " }";
	}

	/**
	 * 获取当前的信息
	 */
	private static String getCurrentInfo() {
		if (!needInfo) {
			return "";
		}
		// int index = 2;
		// StackTraceElement[] eles = new Throwable().getStackTrace();
		// 为什么取4 --> 这个值是得到我调用的那个类的位置，如果还需要更多信息，将要修改这里，
		// 位置不对，自己修改
		int index = 4;
		//
		StackTraceElement[] eles = Thread.currentThread().getStackTrace();
		if (eles.length < (index + 1)) {
			return "Unknown class info !!!";
		}
		StackTraceElement e = eles[index];

		return "FileName : " + e.getFileName() + "||" +
				"ClassName : " + e.getClassName() + "\n" +
				"MethodName : " + e.getMethodName() + "||" +
				"LineNumber : " + e.getLineNumber() + "\n";
	}

}
