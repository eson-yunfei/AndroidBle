package org.eson.liteble.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.shon.bluetooth.core.call.ReadRssiCall;
import com.shon.bluetooth.core.callback.ReadRssiCallback;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 11:57
 * Package name : org.eson.liteble.task
 * Des :
 */
public class ReadRssiTask {

    private String address;
    private String name;
    private FileWrite fileWrite;
    private Handler handler;

    public ReadRssiTask(@NonNull String address, @NonNull String name) {
        this.address = address;
        this.name = name;
        fileWrite = new FileWrite(address);
        handler = new Handler(Looper.getMainLooper());
    }

    public void startTask() {
        if (handler != null) {
            handler.post(runnable);
        }
    }

    public void stopTask() {
        if (handler == null){
            return;
        }
        handler.removeCallbacksAndMessages(null);
        handler = null;
        fileWrite = null;
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            new ReadRssiCall(address)
                    .enqueue(new ReadRssiCallback() {
                        @Override
                        public void onReadRssi(int rssi) {
                            if (handler == null){
                                return;
                            }
                            handler.postDelayed(runnable, 2_000);
                            AsyncTask.execute(() -> {
                                if (fileWrite != null){
                                    fileWrite.saveRssi(address, name, rssi);
                                }
                            });


                        }
                    });

        }
    };

}
