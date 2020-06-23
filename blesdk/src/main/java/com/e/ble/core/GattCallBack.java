package com.e.ble.core;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnStateChangeListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 13:53
 * Package name : com.e.ble.core
 * Des :
 */
final class GattCallBack extends BluetoothGattCallback {

    private static GattCallBack gattCallBack;
    private StateChangedImpl stateChanged;



    public static GattCallBack gattCallBack() {
        if (gattCallBack == null) {
            synchronized (GattCallBack.class) {
                if (gattCallBack == null) {
                    gattCallBack = new GattCallBack();

                }
            }
        }
        return gattCallBack;
    }

//    public static GattCallBack gattCallBack(ConnectBean connectBean) {
//
//        if (gattCallBack == null) {
//            gattCallBack = gattCallBack();
//            assert gattCallBack != null;
//            gattCallBack.addConnectBean(connectBean);
//        } else {
//            gattCallBack.addConnectBean(connectBean);
//        }
//
//        return gattCallBack;
//    }

    public GattCallBack() {
        super();

    }

    public boolean addConnectBean(ConnectBean connectBean) {

        if (stateChanged == null) {
            stateChanged = new StateChangedImpl(connectBean);
            return true;
        } else {
           return stateChanged.addConnectBean(connectBean);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
      if (stateChanged == null){
          stateChanged = new StateChangedImpl();
      }
      stateChanged.setOnStateChangeListener(onStateChangeListener);
    }

    public void setReadListener(OnReadMessage onReadMessage) {

    }





    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (stateChanged != null) {
            stateChanged.onConnectionStateChange(gatt, status, newState);
        }
        super.onConnectionStateChange(gatt, status, newState);

    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (stateChanged != null) {
            stateChanged.onServicesDiscovered(gatt, status);
        }
        super.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
    }

    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyUpdate(gatt, txPhy, rxPhy, status);
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyRead(gatt, txPhy, rxPhy, status);
    }



}
