package org.eson.liteble.ble.command;

import com.e.ble.util.BLEByteUtil;
import com.shon.dispatcher.bean.Listener;
import com.shon.dispatcher.bean.Message;

import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 13:55
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public class CmdListener extends Listener<String> {
    @Override
    public String handlerMessage(Message message) {
        LogUtil.e("CmdListener -->> message : " + message.toString());

        return BLEByteUtil.getHexString(message.getBytes());
    }
}
