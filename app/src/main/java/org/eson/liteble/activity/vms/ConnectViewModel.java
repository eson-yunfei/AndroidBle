package org.eson.liteble.activity.vms;

import android.bluetooth.BluetoothGatt;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.shon.bluetooth.BLEManager;

import org.eson.liteble.DeviceState;
import org.jetbrains.annotations.Nullable;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 16:38
 * Package name : org.eson.liteble.activity.vms
 * Des :
 */
public class ConnectViewModel extends ViewModel {

    private LifecycleOwner lifecycleOwner;
    private ConnectView connectView;

    public void attachView(LifecycleOwner lifecycleOwner, ConnectView connectView) {
        this.lifecycleOwner = lifecycleOwner;
        this.connectView = connectView;
    }

    public void connectDevice(String address, String name) {
        DeviceState deviceState = DeviceState.getInstance();
        if (deviceState == null) {
            return;
        }
        deviceState.connectDevice(address, name)
                .observe(lifecycleOwner, deviceLiveData -> {

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

    public void disConnectDevice(@Nullable String deviceMac) {

        BLEManager.getInstance().disconnectDevice(deviceMac);
    }


    public interface ConnectView {
        void onConnected(String deviceMac, BluetoothGatt gatt);

        void onServerEnable(String deviceMac, BluetoothGatt gatt);

        void onDisconnected(String deviceMac);

        void connectError(String deviceMac, int errorCode);
    }
}
