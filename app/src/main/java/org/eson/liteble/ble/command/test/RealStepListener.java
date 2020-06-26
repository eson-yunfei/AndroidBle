package org.eson.liteble.ble.command.test;

import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.transer.Listener;

import org.eson.liteble.util.ByteUtil;
import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 13:21
 * Package name : org.eson.liteble.ble.command.listener
 * Des : 一款手环的 实时步数解析，
 *       只解析步数，其他的不解析
 */
public class RealStepListener extends Listener<RealSport> {
    @Override
    public RealSport handlerMessage(TMessage tMessage) {
        LogUtil.i("RealStepListener :handlerMessage  " + tMessage.toString());
        byte[] buffer = tMessage.getBytes();

        int size = buffer.length;
        if (size != 8) {
            return null;
        }

        int sportTime = ByteUtil.cbyte2intHigh(buffer, 0, 4);
        int steps = ByteUtil.cbyte2intHigh(buffer, 4, 4);

        RealSport realSport = new RealSport();
        realSport.setSportTime(sportTime);
        realSport.setStep(steps);
        return realSport;
    }
}

