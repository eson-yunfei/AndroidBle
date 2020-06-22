package com.shon.dispatcher;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/22 11:06
 * Package name : com.shon.dispatcher.core
 * Des :
 */
class TransRunnable implements Runnable {

    public CommonCall<?> commonCall;

    public TransRunnable(CommonCall<?> commonCall) {
        this.commonCall = commonCall;
    }


    @Override
    public void run() {
        commonCall.sendData();
    }
}
