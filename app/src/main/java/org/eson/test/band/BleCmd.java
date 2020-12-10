//package org.eson.test.band;
//
///**
// * 蓝牙命令
// */
//public class BleCmd {
//
//
//    /**
//     * 获取温度,气压，海拔
//     */
//    public byte[] getTemperature() {
//        return "$C".getBytes();
//    }
//
//    /**
//     * 获取震动的指令
//     */
//    public byte[] getVibration() {
//        return "$P".getBytes();
//    }
//
//    /**
//     * 防丢开关
//     */
//    public byte[] getLost(boolean isOn) {
//        if (isOn) {
//            return "$LLS1".getBytes();
//        } else {
//            return "$LLS0".getBytes();
//        }
//    }
//
//    /**
//     * 抬腕亮屏
//     *
//     * @param isOn
//     * @return
//     */
//    public byte[] getLightScreen(boolean isOn) {
//        if (isOn) {
//            return "$D1".getBytes();
//        } else {
//            return "$D0".getBytes();
//        }
//    }
//
//    /**
//     * 设置语言
//     */
//    public byte[] getLanguage(boolean isCN) {
//        if (isCN) {
//            return "$LAN1".getBytes();
//        } else {
//            return "$LAN2".getBytes();
//        }
//    }
//
//    /**
//     * 久坐
//     */
//    public byte[] getLongSit(int minute) {
//        return String.format("$J%02d", minute).getBytes();
//    }
//
//    public byte[] getDrink(boolean isOn, int start, int over, int interval, int target) {
//        byte[] buffer = new byte[8];
//        buffer[0] = '$';
//        buffer[1] = 'W';
//        buffer[2] = isOn ? (byte) 1 : 0;
//        buffer[3] = (byte) start;
//        buffer[4] = (byte) over;
//        buffer[5] = (byte) interval;
//        buffer[6] = (byte) (target >> 8);
//        buffer[7] = (byte) target;
//        return buffer;
//    }
//
//
//    /**
//     * 获取闹钟
//     */
//    public byte[] getAlarm(String alarmInfo) {
//        return ("$A" + alarmInfo).getBytes();
//    }
//
//    /**
//     * 获取闹钟
//     */
//    public byte[] getAlarm(boolean isOn1, int hour1, int min1,
//                           boolean isOn2, int hour2, int min2,
//                           boolean isOn3, int hour3, int min3) {
//        StringBuilder buffer = new StringBuilder("$A");
//        if (isOn1) {
//            buffer.append(String.format("1%02d%02d", hour1, min1));
//        } else {
//            buffer.append("00000");
//        }
//        if (isOn2) {
//            buffer.append(String.format("1%02d%02d", hour2, min2));
//        } else {
//            buffer.append("00000");
//        }
//        if (isOn3) {
//            buffer.append(String.format("1%02d%02d", hour3, min3));
//        } else {
//            buffer.append("00000");
//        }
//        return buffer.toString().getBytes();
//    }
//
//    /**
//     * 同步个人信息
//     */
//    public byte[] getSyncUser(int height, int weight, int stepLen, boolean isMetric/*公制?/英制*/, boolean isTime24/*24时制?/12时制*/, boolean isCentigrade/*摄氏度?/华氏度*/) {
//        byte[] buffer = new byte[8];
//        buffer[0] = '$';
//        buffer[1] = 'B';
//        buffer[2] = (byte) height;
//        buffer[3] = (byte) weight;
//        buffer[4] = (byte) stepLen;
//        buffer[5] = (byte) (isMetric ? 0x01 : 0x00);
//        buffer[6] = (byte) (isTime24 ? 0x01 : 0x00);
//        buffer[7] = (byte) (isCentigrade ? 0x00 : 0x01);
//
//
//        return buffer;
//        // String.format("$B%03d%03d%03d", height, weight, stepLen).getBytes();
//    }
//
//    public byte[] getSyncTime(int year, int month, int day, int hour, int minute, int second) {
//        int tYear = year % 100;
//        String format = String.format("$T%02d%02d%02d%02d%02d%02d", tYear, month, day, hour, minute, second);
//        return format.getBytes();
//    }
//
//    /**
//     * 同步屏幕时间
//     */
//    public byte[] getSyncScreenTime(int second) {
//        return String.format("$S%02d", second).getBytes();
//    }
//
//    /**
//     * 获取喝水历史记录
//     *
//     * @return
//     */
//    public byte[] getDrinkRecord() {
//        return "$V".getBytes();
//    }
//
//    public byte[] getBleVersion() {
//        return "$D".getBytes();
//    }
//
//    public byte[] getRebootBLE() {
//        return "$E".getBytes();
//    }
//
//    public byte[] getPulseSwitch(boolean isOn) {
//        if (isOn) {
//            return "$H1".getBytes();
//        } else {
//            return "$H0".getBytes();
//        }
//    }
//
//    /**
//     * 同步数据
//     */
//    public byte[] getSyncData() {
//        return String.format("S").getBytes();
//    }
//
//    /**
//     * 获取发送祝福
//     */
//    public byte[] getWish(int who, int wish) {
//        return String.format("$C%02d%02d", who, wish).getBytes();
//    }
//
//    /**
//     * 获取设置拍照模式
//     */
//    public byte[] getCameraState(boolean isON) {
//        if (isON) {
//            return "$X1".getBytes();
//        } else {
//            return "$X0".getBytes();
//        }
//    }
//
//    public static BleCmd get() {
//        if (instance == null) {
//            instance = new BleCmd();
//        }
//        return instance;
//    }
//
//    private static BleCmd instance;
//
//    private BleCmd() {
//    }
//}
