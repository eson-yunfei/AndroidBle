package org.eson.liteble.ble.command;

import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.annotation.Notice;
import com.shon.dispatcher.call.ListenerCall;
import com.shon.dispatcher.call.SenderCall;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:19
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public interface Command {


    @API(name = SendCmd.class)
    SenderCall<String> sendCmd(String cmd);

    @Notice(name = CmdListener.class)
    ListenerCall<String> startListener();
}
