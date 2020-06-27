package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;
import android.os.DeadObjectException;

import com.e.ble.util.BLELog;
import com.e.tool.ble.BleTool;
import com.e.tool.ble.control.gatt.BGattCallBack;
import com.e.tool.ble.control.gatt.imp.StateChangeListener;
import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.tool.ble.imp.OnStateChanged;

import java.lang.reflect.Method;

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
        try {
            Thread.sleep(300);
            bluetoothGatt.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param onStateChangeListener onStateChangeListener
     */
    public void setSateChangeListener(OnStateChanged onStateChangeListener) {
        stateChangeListener.setOnStateChangeListener(onStateChangeListener);
    }

    private  void refreshCache(BluetoothGatt gatt) throws DeadObjectException {

        try {
            Method localMethod = gatt.getClass().getMethod(
                    "refresh");
            localMethod.invoke(gatt);

        } catch (Exception localException) {
            BLELog.e("An exception occured while refreshing device");
        }
    }

}
