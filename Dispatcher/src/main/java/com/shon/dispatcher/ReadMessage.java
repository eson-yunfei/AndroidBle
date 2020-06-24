package com.shon.dispatcher;

import com.shon.dispatcher.bean.Message;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 15:06
 * Package name : com.shon.dispatcher.core
 * Des :
 */
public class ReadMessage implements Runnable {
    private LinkedBlockingQueue<Message> messages;
    private Invocation invocation;

    public ReadMessage(Invocation invocation) {
        messages = new LinkedBlockingQueue<>();
        this.invocation = invocation;
    }

    public void addMessage(Message receivedData) {
        messages.offer(receivedData);
    }

    @Override
    public void run() {

        while (true) {
            try {
                Message message = messages.take();
                if (invocation == null) {
                    break;
                }
                invocation.handlerMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }


}
