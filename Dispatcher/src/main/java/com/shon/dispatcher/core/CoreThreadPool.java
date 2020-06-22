package com.shon.dispatcher.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/22 10:27
 * Package name : com.shon.dispatcher.core
 * Des :
 */
class CoreThreadPool extends ThreadPoolExecutor {

    private LinkedBlockingQueue<Runnable> workBlockingQueue = new LinkedBlockingQueue<>();
    public CoreThreadPool( BlockingQueue<Runnable> workQueue) {
        super(2, 2, 60, TimeUnit.SECONDS, workQueue);
    }


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        workBlockingQueue.add(r);//保存在运行的任务
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        workBlockingQueue.remove(r);//移除关闭的任务
    }

    /**
     * Description: 正在运行的任务
     *
     * @return LinkedBlockingQueue<Runnable><br>
     */
    LinkedBlockingQueue<Runnable> getWorkBlockingQueue() {
        return workBlockingQueue;
    }
}
