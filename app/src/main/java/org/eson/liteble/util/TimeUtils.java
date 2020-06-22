package org.eson.liteble.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/22 22:41
 * Package name : org.eson.liteble.util
 * Des :
 */
public class TimeUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String getCurrentTime() {
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }
}
