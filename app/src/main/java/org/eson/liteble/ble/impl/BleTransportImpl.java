//package org.eson.liteble.ble.impl;
//
//import com.e.ble.bean.BLECharacter;
//import com.e.back.listener.BLETransportListener;
//import com.e.ble.util.BLEByteUtil;
//import com.shon.dispatcher.TMessage;
//
//import org.eson.liteble.ble.bean.BleDataBean;
//import org.eson.liteble.ble.command.BleTransmitter;
//import org.eson.liteble.util.LogUtil;
//
///**
// * Auth : xiao_yun_fei
// * Date : 2020/6/22 22:58
// * Package name : org.eson.liteble.ble.impl
// * Des :
// */
//public class BleTransportImpl implements BLETransportListener {
//    @Override
//    public void onDesRead(String address) {
//
//    }
//
//    @Override
//    public void onDesWrite(String address) {
//
//    }
//
//    @Override
//    public void onCharacterRead(BLECharacter bleCharacter) {
//
////            Bundle bundle = new Bundle();
//        BleDataBean dataBean = new BleDataBean(bleCharacter.getDeviceAddress(),
//                bleCharacter.getCharacteristicUUID(), bleCharacter.getDataBuffer());
////            bundle.putSerializable(BLEConstant.Type.TYPE_NOTICE, dataBean);
////            RxBus.getInstance().send(bundle);
//
//        TMessage TMessage = new TMessage();
//        TMessage.setBytes(bleCharacter.getDataBuffer());
//        TMessage.setObject(dataBean);
//
//        BleTransmitter.getTransmitter().receiverData(TMessage);
//    }
//
//    @Override
//    public void onCharacterWrite(BLECharacter bleCharacter) {
//
//        LogUtil.e("onCharacterWrite : " + bleCharacter.getDeviceAddress() +
//                " ; "+ BLEByteUtil.getHexString(bleCharacter.getDataBuffer()));
//        TMessage TMessage = new TMessage();
//        TMessage.setBytes(bleCharacter.getDataBuffer());
//        TMessage.setObject(bleCharacter);
//        BleTransmitter.getTransmitter().sendSuccess(TMessage);
//    }
//
//    @Override
//    public void onCharacterNotify(BLECharacter bleCharacter) {
//        BleDataBean dataBean = new BleDataBean(bleCharacter.getDeviceAddress(),
//                bleCharacter.getCharacteristicUUID(), bleCharacter.getDataBuffer());
//
//        TMessage TMessage = new TMessage();
//        TMessage.setBytes(bleCharacter.getDataBuffer());
//        TMessage.setObject(dataBean);
//        BleTransmitter.getTransmitter().receiverData(TMessage);
//
//    }
//}
