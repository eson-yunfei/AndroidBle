package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import androidx.annotation.NonNull;

import com.e.ble.util.BLELog;
import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.message.NotifyState;
import com.e.tool.ble.bean.message.ReadMessage;
import com.e.tool.ble.bean.message.SendMessage;
import com.e.tool.ble.control.gatt.BGattCallBack;
import com.e.tool.ble.control.gatt.imp.CharacteristicListener;
import com.e.tool.ble.imp.OnDataNotify;
import com.e.tool.ble.imp.OnRead;
import com.e.tool.ble.imp.OnWriteDescriptor;

import java.util.UUID;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 19:10
 * Package name : com.e.tool.ble.control.gatt
 * Des :
 */
class ServiceController extends AController {

    private ReaderRunnable readerRunnable;
    private NotifyState notifyState;
    private CharacteristicListener characteristicListener;

    public ServiceController(BleTool bleTool, BGattCallBack bGattCallBack) {
        super(bleTool, bGattCallBack);

        bGattCallBack.setCharacteristicImpl();
        characteristicListener = bGattCallBack.getCharacteristicListener();
    }


    /**
     * @param readMessage
     * @param onReadMessage
     */
    public void readInfo(@NonNull ReadMessage readMessage, @NonNull OnRead onReadMessage) {

        BluetoothGatt bluetoothGatt = getGatt(readMessage.getAddress());

        if (bluetoothGatt == null) {
            onReadMessage.onReadError();
            return;
        }

        ReadRequest readRequest = new ReadRequest(readMessage, bluetoothGatt, onReadMessage);
        if (readerRunnable == null) {
            readerRunnable = new ReaderRunnable();
            corePool.execute(readerRunnable);
        }
        readerRunnable.addRequest(readRequest);
        characteristicListener.setOnReadListener(readerRunnable.getReadMessageListener());

    }


    /**
     * @param sendMessage
     * @return
     */
    public void sendMessage(SendMessage sendMessage) {

        String mac = sendMessage.getAddress();
        final BluetoothGatt bluetoothGatt = getGatt(mac);
        if (bluetoothGatt == null) {
            return;
        }

        UUID serviceUuid = sendMessage.getServiceUUID();
        UUID characteristicUuid = sendMessage.getCharacteristicUUID();
        BluetoothGattCharacteristic characteristic =
                getCharacteristicByUUID(bluetoothGatt, serviceUuid, characteristicUuid);
        if (characteristic == null) {
            return;
        }
        BLELog.i("BLETransport  :: 发送数据-->>:");
        characteristic.setValue(sendMessage.getBytes());
        bluetoothGatt.writeCharacteristic(characteristic);
    }

    private WriteDescriptorRequest writeDescriptorRequest;

    public void updateNotify(NotifyState notifyState,
                             OnWriteDescriptor onUpdateNotify) {
        if (writeDescriptorRequest != null) {
            onUpdateNotify.onWriteDescriptorError();
            return;
        }
        writeDescriptorRequest = new WriteDescriptorRequest(
                notifyState, onUpdateNotify,
                getGatt(notifyState.getAddress()));

        writeDescriptorRequest.launch();
        characteristicListener.setWriteDescriptor(notifyState, new OnWriteDescriptor() {

            @Override
            public void onWriteDescriptorError() {
                writeDescriptorRequest.onWriteDescriptorError();
                writeDescriptorRequest = null;
            }

            @Override
            public void onWriteDescriptor(NotifyState result) {
                writeDescriptorRequest.onWriteDescriptor(result);
                writeDescriptorRequest = null;
            }
        });

    }


    public void listenDataNotify(@NonNull OnDataNotify onDataNotify) {
        characteristicListener.setDataNotifyListener(onDataNotify);
    }

}
