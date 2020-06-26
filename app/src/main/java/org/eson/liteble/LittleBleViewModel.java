package org.eson.liteble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.annotation.LinkState;
import com.e.tool.ble.bean.NotifyMessage;
import com.shon.dispatcher.Dispatcher;
import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.call.ListenerCall;

import org.eson.liteble.ble.command.BleTransmitter;
import org.eson.liteble.ble.command.Command;
import org.eson.liteble.ble.command.test.RealSport;
import org.eson.liteble.util.LogUtil;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 16:31
 * Package name : org.eson.liteble
 * Des :
 */
public class LittleBleViewModel extends AndroidViewModel {

    public static LittleBleViewModel littleBleViewModel = null;

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
    }

    private DeviceState bleDeviceState;

    /**
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
     * @param mac
     * @param state
     */
    private void updateDeviceState(String mac, @LinkState int state) {
        if (bleDeviceState == null) {
            return;
        }
        bleDeviceState.setDeviceState(mac, state);
    }


    private MutableLiveData<NotifyMessage> realData;
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
        private int state;

        public String getMac() {
            return mac;
        }

        public int getState() {
            return state;
        }

        public void setDeviceState(String mac, int state) {
            this.mac = mac;
            this.state = state;
            postValue(this);
        }
    }
}
