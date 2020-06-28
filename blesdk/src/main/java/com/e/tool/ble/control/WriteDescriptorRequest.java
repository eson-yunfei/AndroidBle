package com.e.tool.ble.control;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import com.e.ble.util.BLELog;
import com.e.tool.ble.bean.message.NotifyState;
import com.e.tool.ble.request.Request;
import com.e.tool.ble.imp.OnWriteDescriptor;

import java.util.UUID;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/27 14:26
 * Package name : com.e.tool.ble.control
 * Des :
 */
class WriteDescriptorRequest extends Request {
    private NotifyState notifyState;
    private OnWriteDescriptor onWriteDescriptor;
    private BluetoothGatt gatt;

    WriteDescriptorRequest(NotifyState notifyState, OnWriteDescriptor onUpdateNotify
            , BluetoothGatt gatt) {
        this.notifyState = notifyState;
        this.onWriteDescriptor = onUpdateNotify;
        this.gatt = gatt;
    }


    public void onWriteDescriptorError() {
        isWaiting = false;
        if (onWriteDescriptor != null) {
            onWriteDescriptor.onWriteDescriptorError();
        }
    }

    public void onWriteDescriptor(NotifyState result) {
        isWaiting = false;
        if (onWriteDescriptor != null) {
            onWriteDescriptor.onWriteDescriptor(result);
        }
    }


    @Override
    public boolean launch() {
        writeDescriptor();
        return false;
    }

    private void writeDescriptor() {
        if (notifyState == null) {
            isWaiting = false;
            return;
        }

        UUID serviceUuid = notifyState.getServiceUUID();
        UUID characteristicUuid = notifyState.getCharacteristicUUID();

        BluetoothGattCharacteristic characteristic = getCharacteristicByUUID(gatt,
                serviceUuid,
                characteristicUuid);
        if (characteristic == null) {
            isWaiting = false;
            return;
        }
        UUID descriptorUuid = notifyState.getDesUUID();


       boolean setResult =  gatt.setCharacteristicNotification(characteristic, notifyState.isEnable());//激活通知

        if (!setResult){
            isWaiting = false;
            return;
        }
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUuid);
        if (descriptor == null) {
            BLELog.e("descriptorUuid not find");
            isWaiting = false;
            return;
        }
        if (notifyState.isEnable()) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
       boolean writeResult =  gatt.writeDescriptor(descriptor);
        if (writeResult){
            isWaiting = true;
        }
    }

    /**
     * 获取指定的 GattCharacteristic
     *
     * @param gatt               gatt
     * @param serviceUuid        serviceUuid
     * @param characteristicUuid characteristicUuid
     * @return BluetoothGattCharacteristic
     */
    protected BluetoothGattCharacteristic getCharacteristicByUUID(BluetoothGatt gatt,
                                                                  UUID serviceUuid,
                                                                  UUID characteristicUuid) {
        if (gatt == null) {
            return null;
        }
        BluetoothGattService service = gatt.getService(serviceUuid);
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(characteristicUuid);
    }

}
