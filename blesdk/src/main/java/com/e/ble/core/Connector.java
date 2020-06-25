package com.e.ble.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.text.TextUtils;

import com.e.ble.util.BLELog;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:52
 * Package name : com.e.ble.core
 * Des : 连接器
 */
class Connector implements Runnable {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private LinkedBlockingQueue<ConnectBean> connectBeans = new LinkedBlockingQueue<>();

    Connector(Context context, BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    /**
     * 添加需要连接的设备
     *
     * @param connectBean
     */
    public void addConnect(ConnectBean connectBean) {
        boolean isContains = false;
        for (ConnectBean bean : connectBeans) {
            if (TextUtils.equals(bean.getAddress(), connectBean.getAddress())) {
                isContains = true;
                break;
            }
        }
        if (isContains) {
            BLELog.e("Connector -->> addConnect  () 已存在");
            return;
        }
        connectBeans.add(connectBean);
    }

    @Override
    public void run() {

        while (true) {
            try {

                BLELog.e("Connector -->> run () taking ConnectBean ");
                ConnectBean connectBean = connectBeans.take();
                if (connectBean == null) {
                    BLELog.e("Connector -->> run ()  connectBean is null ");
                    return;
                }
                BLELog.e("Connector -->> run () connectBean : " + connectBean.toString());
                connect(connectBean);

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    /**
     * 连接到指定的设备
     *
     * @param connectBean
     */
    private void connect(ConnectBean connectBean) {
        BLELog.e("Connector -->> connect() ");
        if (bluetoothAdapter == null) {
            BLELog.e("Connector -->> connect() bluetoothAdapter is null  ");
            return;
        }
        final BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(connectBean.getAddress());
        if (bluetoothDevice == null) {
            BLELog.e("Connector -->> connect() bluetoothDevice is null  ");
            return;
        }

        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (!gattCallBack.addConnectBean(connectBean)) {
            BLELog.e("Connector -->> gattCallBack  已存在 该任务");
            return;
        }
        BluetoothGatt gatt = bluetoothDevice.connectGatt(context, false, gattCallBack);
        if (gatt != null) {
            connectBean.setGatt(gatt);
        }
    }
}
