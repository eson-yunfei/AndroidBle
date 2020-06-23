package com.e.ble.core;

import com.e.ble.core.bean.ReadMessage;
import com.e.ble.core.imp.OnConnectListener;
import com.e.ble.core.imp.OnReadMessage;
import com.e.ble.core.imp.OnStateChangeListener;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 16:47
 * Package name : com.e.ble.core
 * Des :
 */
class Tes {

    private void init() {

        Controller controller = BleTool.getInstance().getController();
        controller.connectDevice("", new OnConnectListener() {
            @Override
            public void onConnectSate(int status, int newState) {

            }

            @Override
            public void onServicesDiscovered(String address) {

            }
        });

        controller.setSateChangeListener(new OnStateChangeListener() {

            @Override
            public void onConnecting(String address) {

            }

            @Override
            public void onConnected(String address) {

            }

            @Override
            public void onDisconnecting(String address) {

            }

            @Override
            public void onDisconnected(String address) {

            }
        });

        controller.readInfo(new ReadMessage(), new OnReadMessage() {

        });
    }
}
