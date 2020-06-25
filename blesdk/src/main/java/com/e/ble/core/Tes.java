package com.e.ble.core;

import androidx.annotation.NonNull;

import com.e.ble.core.bean.ConnectBt;
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
            public void onServicesDiscovered(ConnectBt connectBt) {

            }
        });

        controller.setSateChangeListener(new OnStateChangeListener() {


            @Override
            public void onConnecting(@NonNull String address) {

            }

            @Override
            public void onConnected(@NonNull String address) {

            }

            @Override
            public void onDisconnecting(@NonNull String address) {

            }

            @Override
            public void onDisconnected(@NonNull String address) {

            }
        });

        controller.readInfo(new ReadMessage(), new OnReadMessage() {


            @Override
            public void onReadMessage(ReadMessage readMessage) {

            }

            @Override
            public void onReadError() {

            }
        });
    }
}
