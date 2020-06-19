package org.eson.liteble.command;

import com.shon.dispatcher.TransCall;
import com.shon.dispatcher.annotation.API;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:19
 * Package name : org.eson.liteble.command
 * Des :
 */
public interface Command {


    @API(name = SendCmd.class)
    TransCall<String> sendCmd(String cmd);

}