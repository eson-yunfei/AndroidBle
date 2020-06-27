package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;

import androidx.annotation.NonNull;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.message.NotifyState;
import com.e.tool.ble.bean.message.ReadMessage;
import com.e.tool.ble.bean.message.SendMessage;
import com.e.tool.ble.control.gatt.BGattCallBack;
import com.e.tool.ble.imp.OnDataNotify;
import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.tool.ble.imp.OnRead;
import com.e.tool.ble.imp.OnStateChanged;
import com.e.tool.ble.imp.OnWriteDescriptor;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/27 09:58
 * Package name : com.e.tool.ble
 * Des :
 */
public class Controller {
    private BleTool bleTool;
    private BGattCallBack bGattCallBack;

    private StateController stateController;
    private ServiceController serviceController;

    public Controller(BleTool bleTool) {
        this.bleTool = bleTool;
        bGattCallBack = new GattCallBack();
    }

    public BluetoothGatt getGatt(String address) {
        return ((GattCallBack)bGattCallBack).getBluetoothGatt(address);
    }


    /**
     *
     * @param address
     * @param onDevConnectListener
     */
    public void connect(String address, OnDevConnectListener onDevConnectListener) {
        if (stateController == null) {
            stateController = new StateController(bleTool, bGattCallBack);
        }
        stateController.connectDevice(address, onDevConnectListener);
    }

    public void disConnect(String address) {
        if (stateController == null) {
            stateController = new StateController(bleTool, bGattCallBack);
        }
        stateController.disConnect(address);
    }

    public void setSateChangeListener(OnStateChanged onStateChangeListener) {
        if (stateController == null) {
            stateController = new StateController(bleTool, bGattCallBack);
        }
        stateController.setSateChangeListener(onStateChangeListener);
    }

    public void read(ReadMessage readMessage, OnRead onRead) {
        if (serviceController == null){
            serviceController = new ServiceController(bleTool,bGattCallBack);
        }
        serviceController.readInfo(readMessage,onRead);
    }

    public void write(SendMessage sendMessage) {
        if (serviceController == null){
            serviceController = new ServiceController(bleTool,bGattCallBack);
        }
        serviceController.sendMessage(sendMessage);
    }
    public void updateNotify(NotifyState notifyState, OnWriteDescriptor writeDescriptor) {
        if (serviceController == null){
            serviceController = new ServiceController(bleTool,bGattCallBack);
        }
        serviceController.updateNotify(notifyState,writeDescriptor);

    }

    public void listenDataNotify(@NonNull OnDataNotify onDataNotify) {
        if (serviceController == null){
            serviceController = new ServiceController(bleTool,bGattCallBack);
        }
        serviceController.listenDataNotify(onDataNotify);
    }

}
