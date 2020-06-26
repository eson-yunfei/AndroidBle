package com.shon.dispatcher;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:09
 * Package name : com.shon.dispatcher
 * Des : 数据发送，接收入口
 */
public abstract class Transmitter {

    /**
     * 发送 任务
     * @param sendData sendData
     */
    public abstract void sendData(TMessage sendData);


    /**
     * 接收到数据，由外部调用，
     * @param receivedData receivedData
     */
    public void receiverData(TMessage receivedData){

        Dispatcher.getInstance().receiverData(receivedData);
    }


    public  void sendSuccess(TMessage tMessage){
        Dispatcher.getInstance().sendSuccess(tMessage);
    }


}
