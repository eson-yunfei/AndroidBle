package org.eson.liteble.ble.command;

import com.e.ble.util.BLEByteUtil;
import com.shon.dispatcher.transer.Listener;
import com.shon.dispatcher.TMessage;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 13:55
 * Package name : org.eson.liteble.ble.command
 * Des : 通知类数据 返回解析，
 */
public class CmdListener extends Listener<String> {
    @Override
    public String handlerMessage(TMessage tMessage) {

        // 此 demo 中的所有数据，只要是通知过来的数据，都处理，
        // 实际运用中，要根据具体去解析

        return BLEByteUtil.getHexString(tMessage.getBytes());
    }
}
