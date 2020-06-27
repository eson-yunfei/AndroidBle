package org.eson.liteble.activity.vms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.ConnectResult;
import com.e.tool.ble.bean.DevState;
import com.e.tool.ble.imp.OnDevConnectListener;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 16:38
 * Package name : org.eson.liteble.activity.vms
 * Des :
 */
public class ConnectViewModel extends ViewModel {

    private ConnectDeviceData connectDeviceData;

    public ConnectDeviceData connectDevice(String address) {
        connectDeviceData = new ConnectDeviceData();
        BleTool.getInstance()
                .getController()
                .connect(address, getConnectListener());

        return connectDeviceData;
    }

    public void disConnect(String address) {
        BleTool.getInstance().getController()
                .disConnect(address);
    }

    private OnDevConnectListener getConnectListener() {
        return new OnDevConnectListener() {

            @Override
            public void onConnectError(ConnectResult errorCode) {
                if (connectDeviceData != null) {
                    connectDeviceData.setErrorCode(errorCode);
                }
            }

            @Override
            public void onConnectSate(DevState devState) {
                if (connectDeviceData != null) {
                    connectDeviceData.setDevState(devState);
                }
            }

            @Override
            public void onServicesDiscovered(ConnectResult connectBt) {
                if (connectDeviceData != null) {
                    connectDeviceData.setConnectBt(connectBt);
                }
            }
        };
    }


    public static class ConnectDeviceData extends LiveData<ConnectDeviceData> {
        private ConnectResult errorCode;
        private DevState devState;
        private ConnectResult connectBt;

        public ConnectResult getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ConnectResult errorCode) {
            reset();
            this.errorCode = errorCode;
            postValue(this);
        }

        public DevState getDevState() {
            return devState;
        }

        public void setDevState(DevState devState) {
            reset();
            this.devState = devState;
            postValue(this);
        }

        public ConnectResult getConnectBt() {
            return connectBt;
        }

        public void setConnectBt(ConnectResult connectBt) {
            reset();
            this.connectBt = connectBt;
            postValue(this);
        }

        private void reset() {
            errorCode = null;
            devState = null;
            connectBt = null;
        }
    }
}
