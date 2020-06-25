package com.e.tool.ble.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import androidx.annotation.NonNull;

import com.e.tool.ble.BleTool;
import com.e.ble.util.BLELog;
import com.e.tool.ble.bean.NotifyState;
import com.e.tool.ble.bean.ReadMessage;
import com.e.tool.ble.bean.SendMessage;
import com.e.tool.ble.imp.OnDevConnectListener;
import com.e.tool.ble.imp.OnRead;
import com.e.tool.ble.imp.OnStateChanged;
import com.e.tool.ble.imp.OnWriteDescriptor;

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

    public Controller(BleTool bleTool) {
        this.bleTool = bleTool;
        corePool = new CorePool();
    }


    /**
     * 连接到指定 的蓝牙设备
     *
     * @param address
     * @param bleConnListener
     */
    public void connectDevice(String address, OnDevConnectListener bleConnListener) {

        ConnectBean connectBean = new ConnectBean(address, bleConnListener);

        if (connector == null) {
            connector = new Connector(bleTool.getContext(), bleTool.getBluetoothAdapter());
            corePool.execute(connector);
        }
        connector.addConnect(connectBean);

    }

    public void setSateChangeListener(OnStateChanged onStateChangeListener) {
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
    public void readInfo(@NonNull ReadMessage readMessage, @NonNull OnRead onReadMessage) {

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

    private NotifyState notifyState;
    public void updateNotify(NotifyState notifyState, OnWriteDescriptor onUpdateNotify) {
        if (this.notifyState != null){
            onUpdateNotify.onWriteDescriptorError();
            return;
        }
        this.notifyState = notifyState;
        writeDescriptor(onUpdateNotify);


    }

    private void writeDescriptor(OnWriteDescriptor onUpdateNotify) {
        if (notifyState == null){
            return;
        }
        GattCallBack gattCallBack = GattCallBack.gattCallBack();
        if (gattCallBack == null){
           this.notifyState = null;
            onUpdateNotify.onWriteDescriptorError();
            return;
        }
        gattCallBack.setWriteDescriptor(notifyState,new OnWriteDescriptor(){

            @Override
            public void onWriteDescriptorError() {
                notifyState = null;
                onUpdateNotify.onWriteDescriptorError();
            }

            @Override
            public void onWriteDescriptor(NotifyState result) {
                notifyState = null;
                onUpdateNotify.onWriteDescriptor(result);
            }
        });

        BluetoothGatt gatt = getGatt(this.notifyState.getAddress());
        UUID serviceUuid = notifyState.getServiceUUID();
        UUID characteristicUuid = notifyState.getCharacteristicUUID();

        BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(gatt,
                serviceUuid,
                characteristicUuid);
        if (characteristic == null) {
            return;
        }
        UUID descriptorUuid = notifyState.getDesUUID();


        gatt.setCharacteristicNotification(characteristic, notifyState.isEnable());//激活通知

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUuid);
        if (descriptor == null) {
            BLELog.e("descriptorUuid not find");
            return;
        }
        if (notifyState.isEnable()) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        gatt.writeDescriptor(descriptor);
    }

    /**
     * 获取指定的 GattCharacteristic
     *
     * @param serviceUuid
     * @param characteristicUuid
     * @return
     */
    private BluetoothGattCharacteristic getCharacteristicByUUID(
            BluetoothGatt bluetoothGatt,
            UUID serviceUuid,
            UUID characteristicUuid) {
        BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(characteristicUuid);
    }

    public boolean sendMessage(SendMessage sendMessage) {

        final BluetoothGatt bluetoothGatt = getGatt(sendMessage.getAddress());
        if (bluetoothGatt == null) {
            return false;
        }

        UUID serviceUuid = sendMessage.getServiceUUID();
        UUID characteristicUuid = sendMessage.getCharacteristicUUID();
        BluetoothGattCharacteristic characteristic =
                getCharacteristicByUUID(bluetoothGatt, serviceUuid, characteristicUuid);
        if (characteristic == null) {
            return false;
        }
        BLELog.i("BLETransport  :: 发送数据-->>:");
//        BLEByteUtil.printHex(bleUuid.getDataBuffer());
        characteristic.setValue(sendMessage.getBytes());
        return bluetoothGatt.writeCharacteristic(characteristic);

    }
}
