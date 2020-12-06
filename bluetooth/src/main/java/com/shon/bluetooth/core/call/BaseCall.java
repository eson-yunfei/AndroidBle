package com.shon.bluetooth.core.call;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shon.bluetooth.BLEManager;
import com.shon.bluetooth.core.ConnectedDevices;
import com.shon.bluetooth.core.Device;
import com.shon.bluetooth.core.callback.ICallback;
import com.shon.bluetooth.core.callback.OnTimeout;
import com.shon.bluetooth.util.BleLog;

import java.util.UUID;

public abstract class BaseCall<T extends ICallback, C> implements ICall<T> {
    protected T callBack;
    private String address;

    private UUID serviceUUid;
    protected UUID characteristicUUID;

    public BaseCall(String address) {
        this.address = address;
    }

    @SuppressWarnings("unchecked")
    public C setServiceUUid(String serviceUUid) {
        this.serviceUUid = UUID.fromString(serviceUUid);
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    public C setCharacteristicUUID(String characteristicUUID) {
        this.characteristicUUID = UUID.fromString(characteristicUUID);
        return (C) this;
    }

    @NonNull
    @Override
    public String getAddress() {
        return address;
    }

    @NonNull
    @Override
    public T getCallBack() {
        return callBack;
    }

    @Override
    public void enqueue(@NonNull T iCallback) {
        this.callBack = iCallback;

        BLEManager.getInstance().getDataDispatcher().enqueue(this);
    }


    @Nullable
    protected BluetoothGatt bluetoothGatt() {

        ConnectedDevices connectedDevices = BLEManager.getInstance().getConnectDispatcher().getConnectedDevices();
        Device device = connectedDevices.getDevice(address);
        if (device == null ) {
            return null;
        }

        return device.getGatt();
    }

    @Nullable
    protected BluetoothGattCharacteristic gattCharacteristic(@NonNull BluetoothGatt gatt) {
        BluetoothGattService service = gatt.getService(serviceUUid);
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(characteristicUUID);
    }

    private Handler handler;

    protected synchronized void startTimer() {
        if (handler != null) {
            return;
        }
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((OnTimeout)callBack).onTimeout();
                BleLog.d("time out ");
                BLEManager.getInstance().getDataDispatcher().startSendNext(true);
            }
        },3_000);
    }
    public void cancelTimer(){
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
