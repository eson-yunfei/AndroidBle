package org.eson.liteble.ble.command;

import com.e.ble.bean.BLEUuid;
import com.e.ble.core.BleTool;
import com.e.ble.core.bean.SendMessage;
import com.e.ble.util.BLEByteUtil;
import com.shon.dispatcher.Transmitter;
import com.shon.dispatcher.TMessage;

import org.eson.liteble.ble.BleService;
import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 14:12
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public class BleTransmitter extends Transmitter {

    private static BleTransmitter bleTransmitter;
    private BleTransmitter(){}
    public static BleTransmitter getTransmitter(){
        if (bleTransmitter == null){
            synchronized (BleTransmitter.class){
                if (bleTransmitter == null){
                    bleTransmitter = new BleTransmitter();
                }
            }
        }
        return bleTransmitter;
    }
    @Override
    public void sendData(TMessage sendData) {
        byte[] bytes = sendData.getBytes();
        if (bytes == null || bytes.length == 0){
            return;
        }
        SendMessage sendMessage = (SendMessage) sendData.getObject();

        BleTool.getInstance().getController()
                .sendMessage(sendMessage);
        LogUtil.e("接收到发送数据的指令：" + BLEByteUtil.getHexString(sendData.getBytes()));
    }
}
