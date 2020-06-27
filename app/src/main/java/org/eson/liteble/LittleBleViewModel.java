package org.eson.liteble;

import android.app.Application;
import android.bluetooth.BluetoothProfile;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.annotation.LinkState;
import com.e.tool.ble.bean.DevState;
import com.e.tool.ble.bean.message.NotifyMessage;
import com.shon.dispatcher.Dispatcher;
import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.call.ListenerCall;

import org.eson.liteble.ble.command.BleTransmitter;
import org.eson.liteble.ble.command.Command;
import org.eson.liteble.ble.tes.RealSport;
import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 16:31
 * Package name : org.eson.liteble
 * Des : LittleBleViewModel
 */
public class LittleBleViewModel extends AndroidViewModel {

    public static LittleBleViewModel littleBleViewModel = null;
    private BondList bondList;
    private DeviceState bleDeviceState;
    private MutableLiveData<NotifyMessage> realData;


    static void iniViewModel(Application application) {
        if (littleBleViewModel == null) {
            synchronized (LittleBleViewModel.class) {
                if (littleBleViewModel == null) {
                    littleBleViewModel = new LittleBleViewModel(application);
                }
            }
        }
    }

    public static LittleBleViewModel getViewModel() {
        return littleBleViewModel;
    }

    private LittleBleViewModel(@NonNull Application application) {
        super(application);
        bondList = new BondList();
    }


    public BondList getBondList() {
        return bondList;
    }

    /**
     * 监听设备状态
     *
     * @return
     */
    public DeviceState observerDeviceState() {
        if (bleDeviceState == null) {
            bleDeviceState = new DeviceState();
        }

        BleTool.getInstance().getController().setSateChangeListener(this::updateDeviceState);
        return bleDeviceState;
    }

    /**
     * 更新设备状态
     *
     * @param devState
     */
    private void updateDeviceState(DevState devState) {

        LogUtil.e("LittleBleViewModel -->> updateDeviceState : devState  = " + devState.toString());

        if (bondList != null) {
            int state = devState.getNewState();
            if (devState.getNewState() == BluetoothProfile.STATE_CONNECTED) {
                bondList.addBindDevice(devState);
            }
            if (state == BluetoothProfile.STATE_DISCONNECTED) {
                bondList.removeBondDevice(devState);
            }
        }

        if (bleDeviceState != null) {
            bleDeviceState.setDeviceState(devState.getAddress(), devState.getNewState());
        }
    }


    /**
     * 监听通知
     *
     * @return
     */
    public MutableLiveData<NotifyMessage> observerDataNotify() {
        realData = new MutableLiveData<>();
        BleTool.getInstance().getController()
                .listenDataNotify(notifyMessage -> {

                    realData.postValue(notifyMessage);
                    TMessage tMessage = new TMessage();

                    tMessage.setBytes(notifyMessage.getBytes());
                    tMessage.setObject(notifyMessage);
                    BleTransmitter.getTransmitter()
                            .receiverData(tMessage);
                });


        Command command = Dispatcher.getInstance().getApi();
        ListenerCall<RealSport> listener = command.listenRealStep();
        listener.onListener((realSport, tMessage) -> LogUtil.e("realSport : " + realSport.toString()));
        return realData;
    }


    /**
     *
     */
    public static class DeviceState extends LiveData<DeviceState> {
        private String mac;

        @LinkState
        private int state;

        public String getMac() {
            return mac;
        }

        public int getState() {
            return state;
        }

        public void setDeviceState(String mac, @LinkState int state) {
            this.mac = mac;
            this.state = state;
            postValue(this);
        }
    }
}
