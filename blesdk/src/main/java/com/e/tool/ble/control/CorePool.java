package com.e.tool.ble.control;

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
    private ThreadPoolExecutor threadPoolExecutor;

    public CorePool() {
        threadPoolExecutor = new ThreadPoolExecutor(
                2, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
    }


    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

}
