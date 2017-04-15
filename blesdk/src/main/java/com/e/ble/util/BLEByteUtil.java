/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.e.ble.util;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/26
 * @说明：
 */

public class BLEByteUtil {

	/**
	 * c无符号的值，转换成java-int值
	 */
	public static int cbyte2Int(byte byteNum) {
		return byteNum & 0xff;
	}


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


	/**
	 * 把16进制字符串转换成字节数组 * @param hex * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}


}
