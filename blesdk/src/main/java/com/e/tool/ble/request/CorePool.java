package com.e.tool.ble.request;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 15:38
 * Package name : com.e.ble.core
 * Des :
 */
class CorePool {
    private static CorePool corePool = null;
    private ThreadPoolExecutor threadPoolExecutor;

    static CorePool getInstance() {
        if (corePool != null) {
            return corePool;
        }

        synchronized (CorePool.class) {
            if (corePool == null) {
                corePool = new CorePool();
            }
        }
        return corePool;
    }

    private CorePool() {
        threadPoolExecutor = new ThreadPoolExecutor(
                2, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
    }


    void execute(IRunnable<?> runnable) {
        threadPoolExecutor.execute(runnable);
    }

}
