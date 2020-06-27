package com.e.tool.ble.control;

import com.e.tool.ble.bean.message.ReadMessage;
import com.e.tool.ble.control.request.IRunnable;
import com.e.tool.ble.imp.OnRead;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 15:37
 * Package name : com.e.ble.core
 * Des :
 */
class ReaderRunnable extends IRunnable<ReadRequest> {
    private LinkedBlockingDeque<ReadRequest> readRequestDeque;

    private ReadRequest readBean;
    ReaderRunnable() {
        readRequestDeque = new LinkedBlockingDeque<>();
    }


    @Override
    public void addRequest(ReadRequest readRequest) {
        readRequestDeque.offer(readRequest);
    }

    @Override
    protected ReadRequest getNextRequest() throws InterruptedException {
        readBean = readRequestDeque.take();
        return readBean;
    }


    public OnRead getReadMessageListener() {
        return readMessage;
    }
    private OnRead readMessage = new OnRead() {
        @Override
        public void onReadMessage(ReadMessage readMessage) {
            if (readBean != null) {
                readBean.onReadMessage(readMessage);
            }
        }

        @Override
        public void onReadError() {
            if (readBean != null) {
                readBean.onReadError();
            }
        }
    };
}
