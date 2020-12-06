package com.shon.bluetooth.core.call;

import android.bluetooth.BluetoothGatt;

import com.shon.bluetooth.core.callback.ICallback;
import com.shon.bluetooth.core.callback.ReadRssiCallback;
import com.shon.bluetooth.util.BleLog;

public class ReadRssiCall extends BaseCall<ICallback, ReadRssiCallback> {
    public ReadRssiCall(String address) {
        super(address);
    }

    public void readRssi(){
        BluetoothGatt gatt = bluetoothGatt();
        if (gatt == null) {
            BleLog.e("ReadRssiCall gatt is null");
            return;
        }
        gatt.readRemoteRssi();
    }
}
