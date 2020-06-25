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
    public TMessage getSendCmd(TMessage message) {
        return message;
    }

    @Override
    public String handlerMessage(TMessage message) {
        TransLog.e("handlerMessage : " + message.toString());
        return "返回的消息被我处理了";
    }


}
