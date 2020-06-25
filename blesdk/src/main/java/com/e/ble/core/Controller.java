package com.e.ble.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import androidx.annotation.NonNull;

import com.e.ble.core.bean.ReadMessage;
import com.e.ble.core.imp.OnConnectListener;
import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnStateChangeListener;
import com.e.ble.util.BLELog;

import java.util.UUID;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:30
 * Package name : com.e.ble.control
 * Des :
 */
public class Controller {
    private BleTool bleTool;
    private Connector connector;
    private ReaderRunnable readerRunnable;
    private CorePool corePool;

    Controller(BleTool bleTool) {
        this.bleTool = bleTool;
        corePool = new CorePool();
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
            corePool.execute(connector);
        }
        connector.addConnect(connectBean);

    }

    public void setSateChangeListener(OnStateChangeListener onStateChangeListener) {
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (gattCallBack != null) {
            gattCallBack.setOnStateChangeListener(onStateChangeListener);
        }
    }

    /**
     *
     * @param readMessage
     * @param onReadMessage
     */
    public void readInfo(@NonNull ReadMessage readMessage, @NonNull OnReadMessage onReadMessage) {

        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (gattCallBack == null) {
            onReadMessage.onReadError();
           return;
        }

        BluetoothGatt bluetoothGatt = getGatt(readMessage.getAddress());

        if (bluetoothGatt == null) {
            onReadMessage.onReadError();
            return;
        }

        ReadBean readBean = new ReadBean(readMessage, bluetoothGatt,onReadMessage);
        if (readerRunnable == null) {
            readerRunnable = new ReaderRunnable();
            corePool.execute(readerRunnable);
        }
        readerRunnable.addReadBean(readBean);
        gattCallBack.setReadListener(readerRunnable.getReadMessageListener());

    }


    public BluetoothGatt getGatt(String address) {
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        BluetoothGatt gatt = gattCallBack.getBluetoothGatt();
        if (gatt == null) {
            return null;
        }

        BluetoothAdapter adapter = bleTool.getBluetoothAdapter();

        BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(address);

        int state = bleTool.getBluetoothManager().getConnectionState(bluetoothDevice, BluetoothProfile.GATT);

        BLELog.e("state == " + state);
        if (state == BluetoothProfile.STATE_CONNECTED) {
            BLELog.e("address  :  " + address + " ; 已连接");
            return gatt;
        }

        return null;
    }
}
