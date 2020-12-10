//package org.eson.test.band;
//
//import android.util.Log;
//
//import org.es.band.util.ByteUtil;
//public class BleDecode {
//
//    private static final String TAG = "BleDecode";
//
//    private static BleDecode decode;
//
//    private SyncDecoder syncDecoder;
//
//    public static BleDecode get() {
//        if (decode == null) {
//            decode = new BleDecode();
//        }
//        return decode;
//    }
//
//    public void decode(byte[] buffer) {
//        if (buffer == null){
//            return;
//        }
//        int size = buffer.length;
//        switch (size) {
//            case 5:             // 心率数据
////				decodePulse(buffer);
//                break;
//            case 2:             // 心率与其他
//                decodeOther(buffer);
//
//                break;
//            case 4:             // 表示同步数据
//                if (syncDecoder == null){
//                    syncDecoder = new SyncDecoder();
//                }
//                syncDecoder.decodeSync(buffer);
//
//                break;
//            case 8:             // 表示时实显示数据
//                decodeRealTime(buffer);
//                break;
//            case 9:             //温度，气压，海拔
////				decodeAir(buffer);
//                break;
//
//            case 14:
//
//                decodeDrinkRecord(buffer);
//                break;
//        }
//    }
//
//
//    /**
//     * 解析喝水历史记录
//     *
//     * @param buffer
//     */
//    private void decodeDrinkRecord(byte[] buffer) {
//
//        if (buffer == null || buffer.length < 14) {
//            return;
//        }
//
//
//    }
//
//    private void decodeOther(byte[] buffer) {
//        int value = (buffer[0] & 0xff);
//        if (value == 0xff) {            // 拍照
////			onCamera();
//        } else if (value == 0xfe) {     // 心率
////			pulseShow(buffer[1] & 0xff);
//        } else if (value == 0xfd) {
//            boolean isOn = 0 != (buffer[1] & 0xff);
////			pulseStatu(isOn);
//        } else if (value == 0xfc) { // 电池
//            int pow = buffer[1] & 0xff;
////			decodePower(pow);
//        } else if (value == 0xfb) { // 版本
//            float ver = (buffer[1] & 0xff) / 10.0f;
//            String version = String.format("%.1f", ver);
////			decodeVersion(version);
//        } else if (value == 0xfa) {//找手机
////				onFindPhone();
//        }
//    }
//
//
//
//    private void decodeRealTime(byte[] buffer) {
//        int sportTime = ByteUtil.cbyte2intHigh(buffer, 0, 4);
//        int steps = ByteUtil.cbyte2intHigh(buffer, 4, 4);
//        Log.e(TAG, "sportTime:" + sportTime + ";steps:" + steps);
//    }
//
//}
