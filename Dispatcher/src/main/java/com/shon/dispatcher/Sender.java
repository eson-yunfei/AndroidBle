package com.shon.dispatcher;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/22 14:19
 * Package name : com.shon.dispatcher.core
 * Des :
 */
class Sender implements Runnable {
    public LinkedBlockingQueue<TransRunnable> runnableQueue;

    public Sender() {
        runnableQueue = new LinkedBlockingQueue<>();
    }

    public void addTrans(TransRunnable runnable){
        try {
            runnableQueue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {


        try {
            TransRunnable runnable = runnableQueue.take();
            runnable.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
