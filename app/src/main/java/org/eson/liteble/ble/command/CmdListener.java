package org.eson.liteble.ble.command;

import com.e.ble.util.BLEByteUtil;
import com.shon.dispatcher.command.Listener;
import com.shon.dispatcher.TMessage;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 13:55
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public class CmdListener extends Listener<String> {
    @Override
    public String handlerMessage(TMessage TMessage) {

        return BLEByteUtil.getHexString(TMessage.getBytes());
    }
}
