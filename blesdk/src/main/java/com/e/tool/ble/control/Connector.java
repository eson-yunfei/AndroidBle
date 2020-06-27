package com.e.tool.ble.control;

import android.text.TextUtils;

import com.e.ble.util.BLELog;
import com.e.tool.ble.bean.ConnectResult;
import com.e.tool.ble.bean.DevState;
import com.e.tool.ble.control.gatt.imp.StateChangeListener;
import com.e.tool.ble.control.request.IRunnable;
import com.e.tool.ble.imp.OnDevConnectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 11:52
 * Package name : com.e.ble.core
 * Des : 连接器
 */
class Connector extends IRunnable<ConnectRequest> {
    private LinkedBlockingQueue<ConnectRequest> connectRequests = new LinkedBlockingQueue<>();
    private final List<ConnectRequest> cacheList = new ArrayList<>();

    Connector(StateChangeListener stateChangeListener) {

        stateChangeListener.setConnectCallBack(connectListener);
    }


    @Override
    protected void addRequest(ConnectRequest connectRequest) {
        boolean isContains = false;
        for (ConnectRequest bean : connectRequests) {
            if (TextUtils.equals(bean.getAddress(), connectRequest.getAddress())) {
                isContains = true;
                break;
            }
        }
        if (isContains) {
            BLELog.e("Connector -->> addConnect  () 已存在");
            return;
        }
        connectRequests.add(connectRequest);
    }

    @Override
    protected ConnectRequest getNextRequest() throws InterruptedException {
        return connectRequests.take();
    }


    public void addConnectBean(ConnectRequest connectRequest) {
        synchronized (cacheList) {
            boolean isContainsBean = false;
            for (ConnectRequest bean : cacheList) {
                if (TextUtils.equals(bean.getAddress(), connectRequest.getAddress())) {
                    isContainsBean = true;
                    break;
                }
            }

            if (isContainsBean) {
                //存在，即添加设备，
                return;
            }
            //不存在，添加成功
            cacheList.add(connectRequest);
            connectRequests.add(connectRequest);
        }
    }

    /**
     *
     */
    private OnDevConnectListener connectListener = new OnDevConnectListener() {
        @Override
        public void onConnectError(ConnectResult connectResult) {
            final ConnectRequest connectRequest = getConnectBean(connectResult.getAddress());
            if (connectRequest != null){
                connectRequest.onConnectError(connectResult);
                synchronized (cacheList) {
                    cacheList.remove(connectRequest);
                }
            }

        }

        @Override
        public void onConnectSate(DevState devState) {
            final ConnectRequest connectRequest = getConnectBean(devState.getAddress());
            if (connectRequest != null){
                connectRequest.onConnectSate(devState);
            }

        }

        @Override
        public void onServicesDiscovered(ConnectResult result) {
            final ConnectRequest connectRequest = getConnectBean(result.getAddress());
            if (connectRequest != null) {
                connectRequest.onServicesDiscovered(result);
                synchronized (cacheList) {
                    cacheList.remove(connectRequest);
                }
            }
        }
    };

    private ConnectRequest getConnectBean(String address) {
        ConnectRequest connectRequest = null;
        synchronized (cacheList) {
            for (ConnectRequest bean : cacheList) {
                if (TextUtils.equals(bean.getAddress(), address)) {
                    connectRequest = bean;
                    break;
                }
            }
        }
        return connectRequest;
    }

}
