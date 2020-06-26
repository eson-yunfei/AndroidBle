package org.eson.liteble.ble.command;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.SendMessage;
import com.e.ble.util.BLEByteUtil;
import com.shon.dispatcher.Transmitter;
import com.shon.dispatcher.TMessage;

import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 14:12
 * Package name : org.eson.liteble.ble.command
 * Des : 数据的具体传输，这里是蓝牙的，可以扩展到 串口等
 */
public class BleTransmitter extends Transmitter {

    private static BleTransmitter bleTransmitter;

    private BleTransmitter() {
    }

    public static BleTransmitter getTransmitter() {
        if (bleTransmitter == null) {
            synchronized (BleTransmitter.class) {
                if (bleTransmitter == null) {
                    bleTransmitter = new BleTransmitter();
                }
            }
        }
        return bleTransmitter;
    }

    /**
     * 发送数据， 由 sdk 自动调用，不需要手动调用
     * 此方法必须实现
     *
     * @param sendData 需要发送的数据，
     */
    @Override
    public void sendData(TMessage sendData) {
        byte[] bytes = sendData.getBytes();
        if (bytes == null || bytes.length == 0) {
            return;
        }
        SendMessage sendMessage = (SendMessage) sendData.getObject();

        //具体的发送动作
        BleTool.getInstance().getController()
                .sendMessage(sendMessage);
        LogUtil.e("接收到发送数据的指令：" + BLEByteUtil.getHexString(sendData.getBytes()));
    }
}
