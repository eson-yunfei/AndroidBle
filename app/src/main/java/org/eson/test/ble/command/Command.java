//package org.eson.test.ble.command;
//
//import com.shon.dispatcher.annotation.Writer;
//import com.shon.dispatcher.annotation.Notice;
//import com.shon.dispatcher.TMessage;
//import com.shon.dispatcher.call.ListenerCall;
//import com.shon.dispatcher.call.SenderCall;
//
//import org.eson.test.ble.tes.RealSport;
//import org.eson.test.ble.tes.RealStepListener;
//
///**
// * Auth : xiao.yunfei
// * Date : 2020/6/19 11:19
// * Package name : org.eson.test.ble.command
// * Des :  指令总览，类似于网络api
// */
//public interface Command {
//
//
//    /**
//     * 发送数据
//     *
//     * @param sendTMessage sendTMessage 必填，封装需要发送的数据
//     * @return call
//     */
//    @Writer(name = SendCmd.class, timeout = 5 * 100)
//    SenderCall<String> sendCmd(TMessage sendTMessage);
//
//
//    @Notice(name = CmdListener.class)
//    ListenerCall<String> startListener();
//
//
//    @Notice(name = RealStepListener.class)
//    ListenerCall<RealSport> listenRealStep();
//}
