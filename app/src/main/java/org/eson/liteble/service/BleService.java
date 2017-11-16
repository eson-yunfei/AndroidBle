package org.eson.liteble.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.e.ble.bean.BLECharacter;
import com.e.ble.bean.BLEUuid;
import com.e.ble.control.BLEControl;
import com.e.ble.control.listener.BLEConnListener;
import com.e.ble.control.listener.BLEReadRssiListener;
import com.e.ble.control.listener.BLEStateChangeListener;
import com.e.ble.control.listener.BLETransportListener;
import com.e.ble.util.BLEConstant;

import org.eson.liteble.MyApplication;
import org.eson.liteble.RxBus;
import org.eson.liteble.bean.BleDataBean;
import org.eson.liteble.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/24
 * @说明：
 */

public class BleService extends Service {

    private static final String TAG = "BleService";
    private static BleService bleService = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bleService = this;

        BLEControl.get().setBleConnectListener(bleConnectionListener);
        BLEControl.get().setBleStateChangedListener(stateChangeListener);
        BLEControl.get().setBleTransportListener(transportListener);
        BLEControl.get().setBleReadRssiListener(mBLEReadRssiListener);
    }

    public static BleService get() {
        return bleService;
    }

    public void connectionDevice(Context context, String bleMac) {

        BLEControl.get().connectDevice(context, bleMac);
    }

    /**
     * 启用通知
     *
     * @param connectDevice
     * @param serviceUuid
     * @param characteristicUuid
     * @param descriptorUui
     * @param isListenerNotice
     */
    public void enableNotify(String connectDevice, UUID serviceUuid, UUID characteristicUuid,
                             UUID descriptorUui, boolean isListenerNotice) {

        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
                .setAddress(connectDevice)
                .setDescriptorUUID(descriptorUui)
                .setEnable(isListenerNotice).builder();

        BLEControl.get().enableNotify(bleUuid);
    }


    /**
     * 数据的发送
     *
     * @param serviceUuid
     * @param characteristicUuid
     * @param bytes
     */
    public void sendData(UUID serviceUuid, UUID characteristicUuid, byte[] bytes) {
        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
                .setAddress(MyApplication.getInstance().getCurrentShowDevice())
                .setDataBuffer(bytes).builder();

        BLEControl.get().sendData(bleUuid);
    }

    public void readCharacter(UUID serviceUuid, UUID characteristicUuid) {
        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
                .setAddress(MyApplication.getInstance().getCurrentShowDevice()).builder();
        BLEControl.get().readDeviceData(bleUuid);
    }


    BLEConnListener bleConnectionListener = new BLEConnListener() {
        @Override
        public void onConnError(String address, int errorCode) {
            LogUtil.e("address -->>" + address + "; errorCode -->>" + errorCode);
            if (errorCode == 133) {
                BLEControl.get().disconnect(address);
            }
            sendBleState(BLEConstant.Connection.STATE_CONNECT_FAILED, address);
        }

        @Override
        public void onConnSuccess(String address) {

            //更新当前连接的具体的某一个设备
//			MyApplication.getInstance().setCurrentShowDevice(address);
            //添加到已连接的设备列表
//			App.getInstance().addToConnectList(address);
            sendBleState(BLEConstant.Connection.STATE_CONNECT_SUCCEED, address);

        }

        @Override
        public void onAlreadyConnected(String address) {
//			MyApplication.getInstance().setCurrentShowDevice(address);
            sendBleState(BLEConstant.Connection.STATE_CONNECT_CONNECTED, address);
        }
    };


    BLEStateChangeListener stateChangeListener = new BLEStateChangeListener() {
        @Override
        public void onStateConnected(String address) {

        }

        @Override
        public void onStateConnecting(String address) {

        }

        @Override
        public void onStateDisConnecting(String address) {

        }

        @Override
        public void onStateDisConnected(String address) {
            sendBleState(BLEConstant.State.STATE_DIS_CONNECTED, address);
        }
    };

    BLETransportListener transportListener = new BLETransportListener() {

        @Override
        public void onDesRead(String address) {

        }

        @Override
        public void onDesWrite(String address) {

        }

        @Override
        public void onCharacterRead(BLECharacter bleCharacter) {
            Bundle bundle = new Bundle();


            BleDataBean dataBean = new BleDataBean(bleCharacter.getDeviceAddress(),
                    bleCharacter.getCharacteristicUUID(), bleCharacter.getDataBuffer());
            bundle.putSerializable(BLEConstant.Type.TYPE_NOTICE, dataBean);
            RxBus.getInstance().send(bundle);
        }

        @Override
        public void onCharacterWrite(BLECharacter bleCharacter) {

        }

        @Override
        public void onCharacterNotify(BLECharacter bleCharacter) {


            Bundle bundle = new Bundle();


            BleDataBean dataBean = new BleDataBean(bleCharacter.getDeviceAddress(),
                    bleCharacter.getCharacteristicUUID(), bleCharacter.getDataBuffer());
            bundle.putSerializable(BLEConstant.Type.TYPE_NOTICE, dataBean);
            RxBus.getInstance().send(bundle);
        }
    };


    BLEReadRssiListener mBLEReadRssiListener = new BLEReadRssiListener() {
        @Override
        public void onReadRssi(String address, int rssi) {


            saveRssi(address, rssi);

        }

        @Override
        public void onReadRssiError(String address, int errorCode) {

        }
    };

    private File mFile;
    private String info;
    private StringBuffer mBuffer = new StringBuffer();

    private void saveRssi(String address, int rssi) {
        Log.e(TAG, "saveRssi  " + address + " ," + rssi);
        if (!isFileExit()) {
            return;
        }

        String time = getCurrentTime();


        try {
            mBuffer.setLength(0);


            mBuffer.append(time).append(",")
                    .append(String.valueOf(rssi)).append(",")
                    .append("\r\n");
            info =  mBuffer.toString();
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(mFile, true));
            bw.write(info);
            bw.flush();
            Log.e(TAG, "写入成功" + info);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String sd = Environment.getExternalStorageDirectory().getPath() + "/LiteBle/rssi";
    String fileName = sd + "/rssi.csv";

    private boolean isFileExit() {
        mFile = new File(fileName);
        return mFile.exists();
    }

    /**
     * 发送蓝牙状态
     */
    private void sendBleState(int state, String name) {
        Bundle bundle = new Bundle();
        bundle.putInt(BLEConstant.Type.TYPE_STATE, state);
        bundle.putString(BLEConstant.Type.TYPE_NAME, name);
        RxBus.getInstance().send(bundle);
    }


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private String getCurrentTime() {
        String currentTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        return currentTime;
    }

}
