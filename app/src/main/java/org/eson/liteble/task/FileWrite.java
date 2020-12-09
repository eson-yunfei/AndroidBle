package org.eson.liteble.task;

import android.os.Environment;

import org.eson.liteble.util.TimeUtils;
import org.eson.log.LogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/22 22:43
 * Package name : org.eson.liteble.util
 * Des :
 */
class FileWrite {

    private File mFile;
    private StringBuffer mBuffer = new StringBuffer();
    private String appDir = "";
    private String filerDir = "";
    private final String fileName;

    private BufferedWriter bufferedWriter;

    public FileWrite(String address) {
        appDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LiteBle";
        filerDir = appDir + "/rssi";
        fileName = filerDir + "/" + TimeUtils.getCurrentTime() + "_" + address + ".csv";
        LogUtils.e("filerDir " + filerDir);
        LogUtils.e("fileName " + fileName);
        createFileDirs();
    }

    private void createFileDirs() {
        File file = new File(appDir);
        boolean createResult;
        if (file.exists()) {

            file = new File(filerDir);
            if (file.exists()) {
                createFile();
            } else {
                createResult = file.mkdirs();
                if (createResult) {
                    createFileDirs();
                }
            }

        } else {
            createResult = file.mkdirs();
            if (createResult) {
                createFileDirs();
            }
        }

    }

    private void createFile() {
        File file;
        file = new File(fileName);
        if (file.exists()) {
            return;
        }
        boolean createResult = false;
        try {
            createResult = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.e("create file " + fileName + " ; " + createResult);
    }


    private boolean isFileExit() {
        mFile = new File(fileName);
        return mFile.exists();
    }


    public void saveRssi(String address, String name, int rssi) {
        if (!isFileExit()) {
            return;
        }
        mBuffer.setLength(0);
        try {

            mBuffer.append(TimeUtils.getCurrentTime()).append(",")
                    .append(address).append(",")
                    .append(name).append(",")
                    .append(rssi).append(",")
                    .append("\r\n");
            String info = mBuffer.toString();
            //第二个参数意义是说是否以append方式添加内容
            bufferedWriter = new BufferedWriter(new FileWriter(mFile, true));
            bufferedWriter.write(info);
            bufferedWriter.flush();
            LogUtils.e("写入成功 : " + info);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopSave() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            bufferedWriter = null;
        }

    }
}
