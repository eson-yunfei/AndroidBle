package com.shon.dispatcher.core;

import com.shon.dispatcher.TMessage;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 15:06
 * Package name : com.shon.dispatcher.core
 * Des :
 */
public class ReadMessage implements Runnable {
    private LinkedBlockingQueue<TMessage> TMessages;
    private Invocation invocation;

    public ReadMessage(Invocation invocation) {
        TMessages = new LinkedBlockingQueue<>();
        this.invocation = invocation;
    }

    public void addMessage(TMessage receivedData) {
        TMessages.offer(receivedData);
    }


    @Override
    public void run() {

        while (true) {
            try {
                TMessage TMessage = TMessages.take();
                if (invocation == null) {
                    break;
                }
                invocation.handlerMessage(TMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }


}
