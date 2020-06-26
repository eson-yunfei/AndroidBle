package com.shon.dispatcher.core;

import com.shon.dispatcher.TMessage;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/22 11:06
 * Package name : com.shon.dispatcher.core
 * Des : 发送 任务
 */
class SendRunnable implements Runnable {

    //需要发送的队列
    private LinkedBlockingQueue<CommonCall<?>> commonCallList;

    /**
     *
     */
    public SendRunnable() {
        commonCallList = new LinkedBlockingQueue<>();
    }

    /**
     * @param commonCall commonCall
     * @param <T> void
     */
    public <T> void addCall(CommonCall<T> commonCall) {
        commonCallList.add(commonCall);
    }


    /**
     * @param tMessage
     */
    public void sendSuccess(TMessage tMessage) {

        //TODO 回调发送成功
//        if (commonCall == null){
//            return;
//        }
//       if (TextUtils.equals(commonCall.getSendMsg(),message.getBytes().toString())){
//           commonCall.onSendSuccess();
//       }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //当前需要发送的数据
                CommonCall<?> commonCall = commonCallList.take();
                if (!commonCall.sendData()) {
                    return;
                }
                while (commonCall.isWaiting) {
                    Thread.sleep(5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
    }


}
