package com.e.tool.ble.control;

import android.text.TextUtils;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.gatt.BGattCallBack;
import com.e.tool.ble.imp.OnReadRssiCallBack;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/28 16:38
 * Package name : com.e.tool.ble.control
 * Des :
 */
class ReadRssiController extends AController {

    private ReadRssiRequest readRssiRequest;

    public ReadRssiController(BleTool bleTool, BGattCallBack bGattCallBack) {
        super(bleTool, bGattCallBack);
        bGattCallBack.setReadRssiImpl();


    }

    public void readRssi(String address, OnReadRssiCallBack onReadRssiCallBack) {
        if (readRssiRequest != null) {
            return;
        }
        readRssiRequest = new ReadRssiRequest(address, bGattCallBack, onReadRssiCallBack);
        readRssiRequest.launch();

        bGattCallBack.getReadRssiListener().setOnReadRssiCallBack(readRssiCallBack);
    }

    private OnReadRssiCallBack readRssiCallBack = new OnReadRssiCallBack() {
        @Override
        public void onReadRssi(String address, String name, int rssi) {

            if (readRssiRequest != null && TextUtils.equals(address, readRssiRequest.getAddress())) {
                readRssiRequest.onReadRssi(address, name, rssi);
                readRssiRequest = null;
            }

        }
    };
}
