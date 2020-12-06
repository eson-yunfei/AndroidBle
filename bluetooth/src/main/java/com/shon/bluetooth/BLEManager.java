package com.shon.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

/**
 * Auth : xiao.yunfei
 * Date : 2020/9/28 11:51
 * Package name : com.shon.bluetooth
 * Des :
 */
public class BLEManager {
    @SuppressLint("StaticFieldLeak")
    private static BLEManager bleManager = null;
    private final BluetoothManager manager;
    private final GattCallback gattCallback;
    private final Context context;
    private final DataDispatcher dataDispatcher;



    private final ConnectDispatcher connectDispatcher;


    private BLEManager(Context context) {
        this.context = context.getApplicationContext();
        manager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);

        dataDispatcher = new DataDispatcher();
        connectDispatcher = new ConnectDispatcher();
        gattCallback = new GattCallback(connectDispatcher,dataDispatcher);

    }

    @NonNull
    public static BLEManager getInstance() {
        if (bleManager == null)
            throw new NullPointerException("BLEManager 还没有初始化，请先调用 BLEManager.init(Context context) 。");
        return bleManager;
    }

    public static void init(Context context) {
        if (bleManager != null) {
            return;
        }
        synchronized (BLEManager.class) {
            if (bleManager == null) {
                bleManager = new BLEManager(context);
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public BluetoothManager getManager() {
        return manager;
    }


    public GattCallback getGattCallback() {
        return gattCallback;
    }

    public DataDispatcher getDataDispatcher() {
        return dataDispatcher;
    }
    public ConnectDispatcher getConnectDispatcher() {
        return connectDispatcher;
    }

    public void disconnectDevice(@Nullable String mac) {
        connectDispatcher.disconnect(mac);
    }
}