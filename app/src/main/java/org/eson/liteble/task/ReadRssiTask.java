package org.eson.liteble.task;

import android.bluetooth.BluetoothGatt;
import android.text.TextUtils;

import com.e.tool.ble.BleTool;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 11:57
 * Package name : org.eson.liteble.task
 * Des :
 */
public class ReadRssiTask extends Thread {

    private String address;
    private boolean interrupted = false;

    public ReadRssiTask(String address) {
        this.address = address;
    }

    @Override
    public void run() {
        super.run();


        while (!interrupted) {
            if (TextUtils.isEmpty(address)) {
                break;
            }
            BluetoothGatt gatt = BleTool.getInstance().getController().getGatt("");
            if (gatt == null) {
                break;
            }
            boolean readResult = gatt.readRemoteRssi();
            if (readResult) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (interrupted) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        interrupted = true;
    }
}
