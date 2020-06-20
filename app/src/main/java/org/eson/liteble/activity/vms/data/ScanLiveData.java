package org.eson.liteble.activity.vms.data;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.e.ble.bean.BLEDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 15:14
 * Package name : org.eson.liteble.activity.vms.data
 * Des :
 */
public class ScanLiveData extends LiveData<ScanLiveData> {

    private boolean isTimeout = false;
    private boolean isStop = false;

    public List<BLEDevice> deviceList;
    private boolean isFilterNoName;

    public ScanLiveData(boolean isFilterNoName) {
        this.isFilterNoName = isFilterNoName;
        deviceList = new ArrayList<>();
    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void setTimeout(boolean timeout) {
        isTimeout = timeout;
        postValue(this);
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
        postValue(this);
    }


    public List<BLEDevice> getDeviceList() {
        return deviceList;
    }

    public void addScanBLE(final BLEDevice bleDevice) {

        if (isFilterNoName && TextUtils.isEmpty(bleDevice.getName())) {
            //开启过滤无名称设备，并且设备名称为空，不添加设备
            return;
        }
        if (isExitDevice(bleDevice)) {
            updateDevice(bleDevice);
            postValue(this);
            return;
        }
        deviceList.add(bleDevice);
        postValue(this);
    }


    private boolean isExitDevice(BLEDevice device) {
        for (BLEDevice bleDevice : deviceList) {
            if (bleDevice.getMac().equals(device.getMac())) {
                return true;
            }
        }
        return false;
    }

    private void updateDevice(BLEDevice device) {
        for (BLEDevice bleDevice : deviceList) {
            if (bleDevice.getMac().equals(device.getMac())) {
                bleDevice.setRssi(device.getRssi());
                bleDevice.setScanRecord(device.getScanRecord());
            }
        }
    }
}
