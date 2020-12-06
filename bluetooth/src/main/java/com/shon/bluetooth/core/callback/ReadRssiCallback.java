package com.shon.bluetooth.core.callback;

public abstract class ReadRssiCallback implements ICallback ,OnTimeout{
    public abstract void onReadRssi(int rssi);
    @Override
    public boolean process(String address, byte[] result) {
        return false;
    }

    @Override
    public void onTimeout() {

    }
}
