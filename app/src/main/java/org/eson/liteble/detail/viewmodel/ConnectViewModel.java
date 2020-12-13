package org.eson.liteble.detail.viewmodel;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import androidx.annotation.Nullable;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.shon.bluetooth.BLEManager;

import org.eson.liteble.common.DeviceState;
import org.eson.liteble.detail.task.ReadRssiTask;
import org.jetbrains.annotations.NotNull;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 16:38
 * Package name : org.eson.liteble.activity.vms
 * Des :
 */

public class ConnectViewModel extends ViewModel {

    private LifecycleOwner lifecycleOwner;
    private ConnectView connectView;
    private BluetoothDevice device;

    @ViewModelInject
    public ConnectViewModel(@Assisted SavedStateHandle savedStateHandle) {

    }

    public void setCurrentDevice(@NotNull BluetoothDevice device) {
        this.device = device;
    }

    public void attachView(LifecycleOwner lifecycleOwner, @Nullable ConnectView connectView) {
        this.lifecycleOwner = lifecycleOwner;
        this.connectView = connectView;
    }

    public void connectDevice() {
        if (device == null) {
            return;
        }
        DeviceState deviceState = DeviceState.getInstance();
        deviceState.connectDevice(device.getAddress(), device.getName())
                .observe(lifecycleOwner, deviceLiveData -> {

                    if (connectView == null) {
                        return;
                    }
                    switch (deviceLiveData.getState()) {
                        case DeviceState.DeviceLiveData.STATE_CONNECT_ERROR:
                            connectView.connectError(deviceLiveData.getDeviceMac(), deviceLiveData.getErrorCode());
                            break;
                        case DeviceState.DeviceLiveData.STATE_DIS_CONNECTED:
                            connectView.onDisconnected(deviceLiveData.getDeviceMac());
                            break;
                        case DeviceState.DeviceLiveData.STATE_SERVER_ENABLE:
                            connectView.onServerEnable(deviceLiveData.getDeviceMac(), deviceLiveData.getGatt());
                            break;
                        case DeviceState.DeviceLiveData.STATE_CONNECTED:
                            connectView.onConnected(deviceLiveData.getDeviceMac(), deviceLiveData.getGatt());
                            break;
                    }
                });
    }

    public void disConnectDevice() {
        if (device == null) {
            return;
        }
        BLEManager.getInstance().disconnectDevice(device.getAddress());
    }



    private ReadRssiTask readRssiTask;
    public void startReadTimer() {
        if (device == null){
            return;
        }
        if (readRssiTask == null) {
            readRssiTask = new ReadRssiTask(device.getAddress(), device.getName());
        }
        readRssiTask.startTask();
    }

    public void stopReadTimer() {
        if (readRssiTask == null) {
            return;
        }
        readRssiTask.stopTask();
        readRssiTask = null;

    }

    public interface ConnectView {
        void onConnected(String deviceMac, BluetoothGatt gatt);

        void onServerEnable(String deviceMac, BluetoothGatt gatt);

        void onDisconnected(String deviceMac);

        void connectError(String deviceMac, int errorCode);
    }
}
