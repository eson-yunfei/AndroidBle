//package org.eson.liteble.ble.command;
//
//import com.shon.dispatcher.TMessage;
//import com.shon.dispatcher.transer.Sender;
//import com.shon.dispatcher.utils.TransLog;
//
///**
// * Auth : xiao.yunfei
// * Date : 2020/6/19 11:26
// * Package name : org.eson.liteble.ble.command
// * Des : 数据发送 ，
// */
//public class SendCmd extends Sender<String> {
//
//    @Override
//    public TMessage getSendCmd(TMessage message) {
//
//        return message;
//    }
//
//    @Override
//    public String handlerMessage(TMessage message) {
//        TransLog.e("handlerMessage : " + message.toString());
//
//        // 需要处理返回数据时，针对性的做数据解析；
//        // 如果不是 需要解析的数据，返回 null ,
//        // 此出，只发送数据，不做任何处理
//
//        return null;
//    }
//
//
//}
