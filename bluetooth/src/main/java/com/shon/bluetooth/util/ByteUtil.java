package com.shon.bluetooth.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

/**
 * Auth : xiao.yunfei
 * Date : 2020/09/29 22:24
 * Package name : com.shon.bluetooth.util
 * Des :
 */
public class ByteUtil {
    /**
     * c无符号的值，转换成java-int值
     */
    public static int cbyte2Int(byte byteNum) {
        return byteNum & 0xff;
    }

    private static String byteToHex(byte b){
        return String.format("%02x",b).toUpperCase();
    }

    /**
     * 十六进制打印数组
     */
    public static String getHexString(byte[] buffer) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buffer) {
            String s = byteToHex(b);
            sb.append(s);
        }
        return sb.toString();
    }

    public static String byteToCharSequence(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return "";
        }
        String sendString = "";
        try {
            sendString = new String(buffer, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sendString;
    }
    public static String byteToCharSequenceUTF(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return "";
        }
        String sendString = "";
        try {
            sendString = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendString;
    }

    /**
     * 把16进制字符串转换成字节数组 *     @param hex * @return
     */
    public static byte[] hexStringToByte(String hex) {
        if (TextUtils.isEmpty(hex)){
            return null;
        }
        hex = hex.trim();
        hex =  hex.toUpperCase();
        int len = (hex.length() / 2);
        char[] hexChars = hex.toCharArray();
        byte[] result = new byte[len];

        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(hexChars[pos]) << 4 | toByte(hexChars[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        return (byte) HEX_CHARS.indexOf(c);
    }

    private static final String HEX_CHARS = "0123456789ABCDEF";
}
