package org.eson.liteble.ble.command;

import com.shon.dispatcher.bean.Sender;
import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.utils.TransLog;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:26
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public class SendCmd extends Sender<String> {

    @Override
    public Message getSendCmd() {

        Message message = new Message();
        message.setObject(1);
        message.setBytes(new byte[]{1,2,3,4,5,6});
        return message;
    }

    @Override
    public String handlerMessage(Message message) {
        TransLog.e("handlerMessage : " + message.toString());
        return "返回的消息被我处理了";
    }


}