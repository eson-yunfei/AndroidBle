package com.e.ble.core;

import com.e.ble.core.bean.ReadMessage;
import com.e.ble.core.imp.OnConnectListener;
import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnStateChangeListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:30
 * Package name : com.e.ble.control
 * Des :
 */
public class Controller {
    private BleTool bleTool;
    private Connector connector;

    Controller(BleTool bleTool) {
        this.bleTool = bleTool;
    }


    /**
     * 连接到指定 的蓝牙设备
     *
     * @param address
     * @param bleConnListener
     */
    public void connectDevice(String address, OnConnectListener bleConnListener) {

        ConnectBean connectBean = new ConnectBean(address, bleConnListener);

        if (connector == null) {
            connector = new Connector(bleTool.getContext(), bleTool.getBluetoothAdapter());
            connector.start();
        }
        connector.addConnect(connectBean);
    }

    public void setSateChangeListener(OnStateChangeListener onStateChangeListener) {
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (gattCallBack != null) {
            gattCallBack.setOnStateChangeListener(onStateChangeListener);
        }
    }

    public void readInfo(ReadMessage readMessage, OnReadMessage onReadMessage) {
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (gattCallBack != null) {
            gattCallBack.setReadListener(onReadMessage);
        }
    }
}
