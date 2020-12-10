package org.eson.liteble.common.util;

/**
 * Created by Miles
 * on 2015/9/1.
 */
public class ByteUtil {

    /**
     * c无符号的值，转换成java-int值
     */
    public static int cbyte2Int(byte byteNum) {
        return byteNum & 0xff;
    }

    /**
     * c-byte数组转换成java-int值 <br/>
     * 底位在前 <br/>
     * [00 - 08 - 16 - 24]
     * @param buffer byte数组
     * @param index  转换开始位置
     * @param len    转换的长度
     */
    public static int cbyte2intLow(byte[] buffer, int index, int len) {
        if (buffer == null || index + len > buffer.length) return 0;
        int value = 0;
        for (int i = 0; i < len; i++) {
            value += (cbyte2Int(buffer[i + index]) << (i * 8));
        }
        return value;
    }

    /**
     * c-byte数组转换成java-int值 <br/>
     * 高位在前 <br/>
     * [24 - 16 - 08 - 00]
     * @param buffer byte数组
     * @param index  转换开始位置
     * @param len    转换的长度
     */
    public static int cbyte2intHigh(byte[] buffer, int index, int len) {
        if (buffer == null || index + len > buffer.length) return 0;
        int value = 0;
        for (int i = 0, j = len - 1; i < len; i++, j--) {
            value += (cbyte2Int(buffer[i + index]) << (j * 8));
        }
        return value;
    }

    /**
     * int值转换成byte-array <br/>
     * 底位在前 <br/>
     * [00, 08, 16, 24]
     */
    public static byte[] int2ByteLow(int value) {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) value;
        buffer[1] = (byte) (value >> 8);
        buffer[2] = (byte) (value >> 16);
        buffer[3] = (byte) (value >> 24);
        return buffer;
    }

    /**
     * int值转换成byte-array <br/>
     * 高位在前 <br/>
     * [24, 16, 08, 00]
     */
    public static byte[] int2ByteHigh(int value) {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) (value >> 24);
        buffer[1] = (byte) (value >> 16);
        buffer[2] = (byte) (value >> 8);
        buffer[3] = (byte) value;
        return buffer;
    }

    /**
     * 十六进制打印数组
     */
    public static void printHex(byte[] buffer) {
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
//        LogUtil.e(sb.toString());
    }

    /**
     * 十进制打印数组
     */
    public static void print(byte[] buffer) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (byte b : buffer) {
            sb.append("  ").append(b & 0xff);
        }
        sb.append("  ]");
//        LogUtil.e(sb.toString());
    }

    /**
     * 十六进制打印数组
     */
    public static String getFormatHexString(byte[] buffer) {
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
}