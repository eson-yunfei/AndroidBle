package org.eson.liteble.ble.command;

import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.command.Sender;
import com.shon.dispatcher.utils.TransLog;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:26
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public class SendCmd extends Sender<String> {

    @Override
    public TMessage getSendCmd(TMessage TMessage) {
//        Message message = new Message();
//        String serviceUUID = (String) params.get("serviceUUID");
//        String characterUUID = (String) params.get("characterUUID");
//        byte[] buffer = (byte[]) params.get("buffer");

//        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(UUID.fromString(serviceUUID),
//                UUID.fromString(characterUUID))
//                .setAddress(MyApplication.getInstance().getCurrentShowDevice())
//                .setDataBuffer(buffer).builder();
////
//        message.setBytes(bleUuid.getDataBuffer());
//        message.setObject(bleUuid);
        return TMessage;
    }

    @Override
    public String handlerMessage(TMessage TMessage) {
        TransLog.e("handlerMessage : " + TMessage.toString());
        return "返回的消息被我处理了";
    }


}
