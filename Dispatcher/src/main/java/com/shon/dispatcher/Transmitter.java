package com.shon.dispatcher;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:09
 * Package name : com.shon.dispatcher
 * Des :
 */
public abstract class Transmitter {
    public abstract void sendData(TMessage sendData);

    public void receiverData(TMessage receivedData){

        Dispatcher.getInstance().receiverData(receivedData);
    }

    public  void sendSuccess(TMessage TMessage){
        Dispatcher.getInstance().sendSuccess(TMessage);
    }


}
