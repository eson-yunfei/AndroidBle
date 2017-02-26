package org.eson.ble_sdk.util;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/26
 * @说明：
 */

public class BLEByteUtil {


	/**
	 * 十六进制打印数组
	 */
	public static void printHex(byte[] buffer) {

		BLELog.e(getHexString(buffer));
	}

	/**
	 * 十进制打印数组
	 */
	public static void print(byte[] buffer) {
		BLELog.e(getString(buffer));
	}


	/**
	 * 十六进制打印数组
	 */
	public static String getHexString(byte[] buffer) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (byte b : buffer) {
			String intS = Integer.toHexString(b & 0xff);
			if (intS.length() == 1) {
				sb.append("  ").append("0").append(intS);
			} else {
				sb.append("  ").append(intS);
			}
		}
		sb.append("  ]");
		return sb.toString();
	}

	/**
	 * 十进制打印数组
	 */
	public static String getString(byte[] buffer) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (byte b : buffer) {
			sb.append("  ").append(b & 0xff);
		}
		sb.append("  ]");
		return sb.toString();
	}

}
