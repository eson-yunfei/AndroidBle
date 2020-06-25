package org.eson.liteble.ble;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

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

//        BLEControl.get().setBleConnectListener(new BleConnectionImpl());
//        BLEControl.get().setBleStateChangedListener(new StateChangeImpl());
//        BLEControl.get().setBleTransportListener(new BleTransportImpl());
//        BLEControl.get().setBleReadRssiListener(new ReadRssiImpl());
    }

    public static BleService get() {
        return bleService;
    }

//    public void connectionDevice(String bleMac) {
//
//        BLEControl.get().connectDevice(getApplicationContext(), bleMac);
//    }

    /**
     * 启用通知
     *
     * @param connectDevice
     * @param serviceUuid
     * @param characteristicUuid
     * @param descriptorUui
     * @param isListenerNotice
     */
//    public void enableNotify(String connectDevice, UUID serviceUuid, UUID characteristicUuid,
//                             UUID descriptorUui, boolean isListenerNotice) {
//
//        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
//                .setAddress(connectDevice)
//                .setDescriptorUUID(descriptorUui)
//                .setEnable(isListenerNotice).builder();
//
//        BLEControl.get().enableNotify(bleUuid);
//    }


//    /**
//     * 数据的发送
//     *
//     * @param serviceUuid
//     * @param characteristicUuid
//     * @param bytes
//     */
//    public void sendData(UUID serviceUuid, UUID characteristicUuid, byte[] bytes) {
//        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
//                .setAddress(MyApplication.getInstance().getCurrentShowDevice())
//                .setDataBuffer(bytes).builder();
//
//        BLEControl.get().sendData(bleUuid);
//    }

//    public void readCharacter(UUID serviceUuid, UUID characteristicUuid) {
//        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
//                .setAddress(MyApplication.getInstance().getCurrentShowDevice()).builder();
//        BLEControl.get().readDeviceData(bleUuid);
//    }

}
