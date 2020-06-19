package com.shon.dispatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/19 20:13
 * Package name : com.shon.dispatcher.core
 * Des :
 */
 class CmdDispatcher {
    private ExecutorService mWorkThreadExecutor;


    public void createDispatcher(){
        mWorkThreadExecutor =  new ThreadPoolExecutor(
                1,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public void launch(){
        mWorkThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
