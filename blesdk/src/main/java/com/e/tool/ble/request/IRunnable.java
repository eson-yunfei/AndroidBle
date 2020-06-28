package com.e.tool.ble.request;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/27 13:19
 * Package name : com.e.tool.ble.request
 * Des :
 */
public abstract class IRunnable<T extends Request> implements Runnable {
    @Override
    public void run() {

        while (true) {
            try {
                T i = getNextRequest();
                if (i == null) {
                    continue;
                }
                boolean launchResult = i.launch();

                if (launchResult) {
                    while (i.isWaiting) {
                        Thread.sleep(10);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
    }

    protected void addRunnable() {
        CorePool.getInstance().execute(this);
    }

    protected abstract void addRequest(T t);

    protected abstract T getNextRequest() throws InterruptedException;
}
