package com.shon.bluetooth.core.call;

import androidx.annotation.NonNull;

import com.shon.bluetooth.BLEManager;
import com.shon.bluetooth.core.callback.ICallback;

public class Listener implements ICall<ICallback> {
    private String address;
    private ICallback iCallback;

    public Listener(String address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String getAddress() {
        return address;
    }

    @NonNull
    @Override
    public ICallback getCallBack() {
        return iCallback;
    }

    @Override
    public void enqueue(@NonNull ICallback iCallback) {
        this.iCallback = iCallback;

        BLEManager.getInstance().getDataDispatcher().enqueue(this);
    }
}
