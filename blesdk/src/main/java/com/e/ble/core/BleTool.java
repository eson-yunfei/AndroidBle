package com.e.ble.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:37
 * Package name : com.e.ble.core
 * Des :
 */
public class BleTool {

    private Context context;
    private Controller controller;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    private static BleTool bleTool;

    public static BleTool getInstance() {
        if (bleTool == null) {
            synchronized (BleTool.class) {
                if (bleTool == null) {
                    bleTool = new BleTool();
                }
            }
        }
        return bleTool;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public Controller getController() {
        if (controller == null) {
            synchronized (BleTool.class) {
                if (controller == null) {
                    controller = new Controller(this);
                }
            }
        }
        return controller;
    }


    public BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }
}
