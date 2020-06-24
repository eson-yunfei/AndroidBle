package com.shon.dispatcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/22 10:31
 * Package name : com.shon.dispatcher.core
 * Des :
 */
class ThreadPool {

    private static ThreadPool threadPool;
    private static int keepAliveTime = 1000;
    private CoreThreadPool poolExecutor;

    public static ThreadPool getThreadPool() {
        if (threadPool == null) {
            synchronized (ThreadPool.class) {
                if(threadPool == null) {
                    threadPool = new ThreadPool();
                }
            }
        }
        return threadPool;
    }


    private ThreadPool() {
        if (poolExecutor == null) {
            poolExecutor = new CoreThreadPool(new LinkedBlockingQueue<Runnable>(2));
        }

    }

    public void addTask(Runnable runnable) {
        BlockingQueue<Runnable> waitThreadQueue = poolExecutor.getQueue();//Returns the task queue
        LinkedBlockingQueue<Runnable> workThreadQueue = poolExecutor.getWorkBlockingQueue();//Returns the running work


        if (!waitThreadQueue.contains(runnable) && !workThreadQueue.contains(runnable)) {
            //判断任务是否存在正在运行的线程或存在阻塞队列，
            // 不存在的就加入线程池（这里的比较要重写equals()）
            poolExecutor.execute(runnable);//添加到线程池
        }
    }

}
