package org.eson.liteble.task;

import android.text.TextUtils;

import com.e.tool.ble.BleTool;

import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 11:57
 * Package name : org.eson.liteble.task
 * Des :
 */
public class ReadRssiTask extends Thread {

    private String address;
    private boolean interrupted = false;
    private FileWrite fileWrite;

    public ReadRssiTask(String address) {
        this.address = address;
        fileWrite = new FileWrite(address);
    }

    @Override
    public void run() {
        super.run();


        while (!interrupted) {
            if (TextUtils.isEmpty(address)) {
                break;
            }

            BleTool.getInstance().getController()
                    .readRssi(address, (address, name, rssi) -> {
                        LogUtil.e("address : " + address + " ; name :" + name + " ; rssi : " + rssi);
                        if (fileWrite == null) {
                            return;
                        }
                        fileWrite.saveRssi(address,name, rssi);
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (interrupted) {
                break;
            }

        }
    }

    public void stopRead() {
        interrupted = true;
        if (fileWrite != null){
            fileWrite.stopSave();
            fileWrite = null;
        }

    }


}
