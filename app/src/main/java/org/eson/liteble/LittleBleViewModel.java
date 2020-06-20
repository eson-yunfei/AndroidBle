package org.eson.liteble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.e.ble.annotation.BLESate;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 16:31
 * Package name : org.eson.liteble
 * Des :
 */
public class LittleBleViewModel extends AndroidViewModel {

    public static LittleBleViewModel littleBleViewModel = null;

    static void iniViewModel(Application application) {
        if (littleBleViewModel == null) {
            synchronized (LittleBleViewModel.class) {
                if (littleBleViewModel == null) {
                    littleBleViewModel = new LittleBleViewModel(application);
                }
            }
        }
    }

    public static LittleBleViewModel getViewModel() {
        return littleBleViewModel;
    }

    private LittleBleViewModel(@NonNull Application application) {
        super(application);
    }

    private DeviceState bleDeviceState ;

    public DeviceState observerDeviceState() {
        if (bleDeviceState== null){
            bleDeviceState = new DeviceState();
        }
        return bleDeviceState;
    }

    public void updateDeviceState(String mac, @BLESate int state){
        if (bleDeviceState == null){
            return;
        }
        bleDeviceState.setDeviceState(mac, state);
    }


    public static class DeviceState extends LiveData<DeviceState>{
        private String mac;
        private int state;

        public String getMac() {
            return mac;
        }

        public int getState() {
            return state;
        }

        public void setDeviceState(String mac, int state){
            this.mac = mac;
            this.state = state;
            postValue(this);
        }
    }
}
