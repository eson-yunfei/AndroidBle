//package org.eson.band;
//
//import android.util.Log;
//
//import org.es.band.bean.Sport;
//
//import java.util.Calendar;
//
///**
// * package_name  : org.es.ble
// * file_name     : SyncDecoder
// * create by     : xiaoyunfei
// * create date   : 2018/5/19
// * description   :
// */
//class SyncDecoder {
//
//    private static final String TAG = "SyncDecoder";
//    private int sportMin;
//    private int sleepMin;
//
//    private void resetMark() {
//        Calendar calendar = Calendar.getInstance();
//        int H = calendar.get(Calendar.HOUR_OF_DAY); // 当前小时
//        int M = calendar.get(Calendar.MINUTE);      // 当前分钟
//        // 记录时间
//        sportMin = H * 60 + (M / 5) * 5;
//        sleepMin = sportMin;
//    }
//
//    SyncDecoder() {
//        resetMark();
//    }
//
//    void decodeSync(byte[] buffer) {
//        if ((buffer[2] & 0xf0) == 0x80) {
//            // 睡眠数据
////			Sleep sleep = new Sleep(buffer);
////			syncSleep(sleep);
//        } else {
//            // 运动数据
//            decoderSport(buffer);
//        }
//    }
//
//
//    private void decoderSport(byte[] buffer) {
//
//
//        Sport sport = new Sport(buffer);
//        if (sport.getSteps() <= 0) {
//            return;
//        }
//        int tM = sportMin - sport.getMinuteByDay();
//        if (tM <= 0) {
//            int cutDay = Math.abs(tM / 1440) + 1;  // 表示向前推的时间
//            Calendar calendar = Calendar.getInstance();  // 超到昨天或者更早，重新算日期
//            calendar.add(Calendar.DAY_OF_YEAR, -cutDay);
//            sport.setYear(calendar.get(Calendar.YEAR));
//            sport.setMonth(calendar.get(Calendar.MONTH) + 1);
//            sport.setDay(calendar.get(Calendar.DAY_OF_MONTH));
//            sport.setDate(getCalendarDate(calendar));
//            // 重新计算当前的时间分钟数
//            tM = cutDay * 1440 + tM;
//        }
//
//        sport.setMinuteByDay(tM);
//        if (!sport.getDate().equals(getCalendarDate(Calendar.getInstance()))){
//            Log.e(TAG, "今天数据同步完成");
//        }
//
//        Log.e(TAG, sport.toString());
//
//    }
//
//    /**
//     * 获取Calendar上面的日期表示  2015-12-17
//     */
//    private String getCalendarDate(Calendar calendar) {
//        return String.format("%04d-%02d-%02d",
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH) + 1,
//                calendar.get(Calendar.DAY_OF_MONTH));
//    }
//}
