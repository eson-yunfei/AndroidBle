package com.shon.dispatcher;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/22 11:06
 * Package name : com.shon.dispatcher.core
 * Des :
 */
class TransRunnable implements Runnable {

    private LinkedBlockingQueue<CommonCall> commonCallList;

    public TransRunnable() {
        commonCallList = new LinkedBlockingQueue<>();
    }

    public <T> void addCall(CommonCall<T> commonCall) {
        commonCallList.add(commonCall);
    }

    @Override
    public void run() {
        while (true) {
            try {
                CommonCall<?> commonCall = commonCallList.take();
                if (commonCall.sendData()) {
                    while (commonCall.isWaiting) {
                        Thread.sleep(5);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
    }
}
