package com.shon.dispatcher;

import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.utils.TransLog;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:09
 * Package name : com.shon.dispatcher
 * Des :
 */
public abstract class Transmitter {
    public abstract void sendData(Message sendData);

    public void receiverData(Message receivedData){

        TransLog.e("receiverData : " + receivedData.toString());
        Dispatcher.getInstance().receiverData(receivedData);
    }
}
