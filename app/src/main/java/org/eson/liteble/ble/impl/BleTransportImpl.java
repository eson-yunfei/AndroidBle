package org.eson.liteble.ble.impl;

import com.e.ble.bean.BLECharacter;
import com.e.ble.control.listener.BLETransportListener;
import com.shon.dispatcher.bean.Message;

import org.eson.liteble.ble.bean.BleDataBean;
import org.eson.liteble.ble.command.BleTransmitter;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/22 22:58
 * Package name : org.eson.liteble.ble.impl
 * Des :
 */
public class BleTransportImpl implements BLETransportListener {
    @Override
    public void onDesRead(String address) {

    }

    @Override
    public void onDesWrite(String address) {

    }

    @Override
    public void onCharacterRead(BLECharacter bleCharacter) {

//            Bundle bundle = new Bundle();
        BleDataBean dataBean = new BleDataBean(bleCharacter.getDeviceAddress(),
                bleCharacter.getCharacteristicUUID(), bleCharacter.getDataBuffer());
//            bundle.putSerializable(BLEConstant.Type.TYPE_NOTICE, dataBean);
//            RxBus.getInstance().send(bundle);

        Message message = new Message();
        message.setBytes(bleCharacter.getDataBuffer());
        message.setObject(dataBean);

        BleTransmitter.getTransmitter().receiverData(message);
    }

    @Override
    public void onCharacterWrite(BLECharacter bleCharacter) {

    }

    @Override
    public void onCharacterNotify(BLECharacter bleCharacter) {
        BleDataBean dataBean = new BleDataBean(bleCharacter.getDeviceAddress(),
                bleCharacter.getCharacteristicUUID(), bleCharacter.getDataBuffer());

        Message message = new Message();
        message.setBytes(bleCharacter.getDataBuffer());
        message.setObject(dataBean);
        BleTransmitter.getTransmitter().receiverData(message);

    }
}
