package org.eson.liteble.ble.impl;

import com.e.ble.control.listener.BLEReadRssiListener;

import org.eson.liteble.util.FileUtil;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/22 22:56
 * Package name : org.eson.liteble.ble.impl
 * Des :
 */
public class ReadRssiImpl implements BLEReadRssiListener {
    private FileUtil fileUtil;

    @Override
    public void onReadRssi(String address, int rssi) {
        if (fileUtil == null) {
            fileUtil = new FileUtil();
        }
        fileUtil.saveRssi(address, rssi);
    }

    @Override
    public void onReadRssiError(String address, int errorCode) {

    }
}
