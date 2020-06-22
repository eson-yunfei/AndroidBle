package org.eson.liteble.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/22 22:43
 * Package name : org.eson.liteble.util
 * Des :
 */
public class FileUtil {

    private File mFile;
    private String info;
    private StringBuffer mBuffer = new StringBuffer();
    String sd = Environment.getExternalStorageDirectory().getPath() + "/LiteBle/rssi";
    String fileName = sd + "/rssi.csv";



    private boolean isFileExit() {
        mFile = new File(fileName);
        return mFile.exists();
    }


    public void saveRssi(String address, int rssi) {
        LogUtil.e("saveRssi  " + address + " ," + rssi);
        if (!isFileExit()) {
            return;
        }
        mBuffer.setLength(0);
        try {

            mBuffer.append(TimeUtils.getCurrentTime()).append(",")
                    .append(String.valueOf(rssi)).append(",")
                    .append("\r\n");
            info = mBuffer.toString();
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(mFile, true));
            bw.write(info);
            bw.flush();
            LogUtil.e("写入成功" + info);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
