package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;

import com.e.ble.util.BLELog;
import com.e.tool.ble.BleTool;
import com.e.tool.ble.control.gatt.BGattCallBack;
import com.e.tool.ble.control.gatt.imp.StateChangeListener;
import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.tool.ble.imp.OnStateChanged;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:30
 * Package name : com.e.ble.control
 * Des : 状态管理
 */
public class StateController extends AController {

    private Connector connector;
    private StateChangeListener stateChangeListener;

    public StateController(BleTool bleTool, BGattCallBack gattCallback) {
        super(bleTool, gattCallback);
        gattCallback.setStateChangeImpl();
        stateChangeListener = gattCallback.getStateChangeListener();
    }


    /**
     * 连接到指定 的蓝牙设备
     *
     * @param address
     * @param bleConnListener
     */
    public void connectDevice(String address, OnDevConnectListener bleConnListener) {

        ConnectRequest connectRequest = new ConnectRequest(bleTool.getContext(),
                address,
                bleTool.getBluetoothAdapter(),
                bGattCallBack, bleConnListener);

        if (connector == null) {
            connector = new Connector(stateChangeListener);
            corePool.execute(connector);
        }
        connector.addConnectBean(connectRequest);

    }

    public void disConnect(String address) {
        BLELog.e("Controller : disConnect :" + address);

        BluetoothGatt bluetoothGatt = ((GattCallBack) bGattCallBack).getBluetoothGatt(address);
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.disconnect();
        bluetoothGatt.close();
    }

    /**
     * @param onStateChangeListener onStateChangeListener
     */
    public void setSateChangeListener(OnStateChanged onStateChangeListener) {
        stateChangeListener.setOnStateChangeListener(onStateChangeListener);
    }


}
