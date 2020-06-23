package com.e.ble.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:52
 * Package name : com.e.ble.core
 * Des :
 */
class Connector extends Thread {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private LinkedBlockingQueue<ConnectBean> connectBeans = new LinkedBlockingQueue<>();

    Connector(Context context, BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void addConnect(ConnectBean connectBean) {
        connectBeans.add(connectBean);
    }

    @Override
    public void run() {

        while (true) {
            try {
                ConnectBean connectBean = connectBeans.take();
                if (connectBean == null) {
                    return;
                }

                connect(connectBean);

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    private void connect(ConnectBean connectBean) {
        final BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(connectBean.getAddress());
        if (bluetoothDevice == null) {
            return;
        }
        BluetoothGatt gatt = bluetoothDevice.connectGatt(context, false, GattCallBack.gattCallBack(connectBean));
        if (gatt != null) {
            connectBean.setGatt(gatt);
        }
    }
}
