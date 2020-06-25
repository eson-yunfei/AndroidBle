package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.e.tool.ble.bean.ReadMessage;
import com.e.tool.ble.imp.OnRead;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 15:37
 * Package name : com.e.ble.core
 * Des :
 */
class ReaderRunnable implements Runnable {
    private LinkedBlockingDeque<ReadBean> readBeanDeque;

    private ReadBean readBean;

    ReaderRunnable() {
        readBeanDeque = new LinkedBlockingDeque<>();
    }

    public void addReadBean(ReadBean readBean) {
        readBeanDeque.offer(readBean);
    }

    @Override
    public void run() {

        while (true) {
            try {
                readBean = readBeanDeque.take();

                if (readBean == null) {
                    return;
                }

                if (!readInfo()) {
                    return;
                }


                while (readBean.isWaiting) {
                    Thread.sleep(10);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }


    private boolean readInfo() {
        if (readBean == null) {
            return false;
        }
        UUID serviceUuid = readBean.getReadBean().getServiceUUID();
        UUID characteristicUuid = readBean.getReadBean().getCharacteristicUUID();

        BluetoothGattCharacteristic characteristic =
                getCharacteristicByUUID(readBean.getGatt(),
                        serviceUuid,
                        characteristicUuid);
        if (characteristic == null) {
            readBean.getOnRead().onReadError();
            return false;
        }
        if (readBean.getGatt().readCharacteristic(characteristic)) {
            return true;
        } else {
            readBean.getOnRead().onReadError();
            return false;
        }

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

    public OnRead getReadMessageListener() {
        return readMessage;
    }
    private OnRead readMessage = new OnRead() {
        @Override
        public void onReadMessage(ReadMessage readMessage) {
            if (readBean != null) {
                readBean.onReadMessage(readMessage);
            }
        }

        @Override
        public void onReadError() {
            if (readBean != null) {
                readBean.onReadError();
            }
        }
    };
}
