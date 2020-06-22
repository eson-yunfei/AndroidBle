package org.eson.liteble.ble.command;

import com.shon.dispatcher.TransCall;
import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.annotation.Notice;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:19
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public interface Command {


    @API(name = SendCmd.class)
    TransCall<String> sendCmd(String cmd);

    @Notice(name = CmdListener.class)
    TransCall<String> startListener();
}
