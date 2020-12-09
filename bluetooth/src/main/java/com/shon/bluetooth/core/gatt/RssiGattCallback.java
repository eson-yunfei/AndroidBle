package com.shon.bluetooth.core.gatt;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.shon.bluetooth.DataDispatcher;
import com.shon.bluetooth.core.Result;
import com.shon.bluetooth.core.annotation.Constants;

/**
 * Auth : xiao.yunfei
 * Date : 2020/9/28 16:26
 * Package name : com.shon.bluetooth.core.gatt
 * Des :
 */
public class RssiGattCallback {

    private DataDispatcher dataDispatcher;

    public RssiGattCallback(DataDispatcher dataDispatcher) {
        this.dataDispatcher = dataDispatcher;
    }

    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

        Result result = new Result(Constants.PROPERTY_READ_RSSI);
        result.setAddress(gatt.getDevice().getAddress());
        result.setBytes(new byte[]{(byte) (rssi * -1)});
        dataDispatcher.onReceivedResult(result);
    }
}