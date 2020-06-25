package org.eson.liteble.activity.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.e.tool.ble.bean.SendMessage;
import com.e.ble.util.BLEByteUtil;
import com.shon.dispatcher.Dispatcher;
import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.call.SenderCall;
import com.shon.dispatcher.imp.OnMsgSendListener;

import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.ble.command.Command;
import org.eson.liteble.databinding.ActivitySendDataBinding;
import org.eson.liteble.util.LogUtil;

import java.util.UUID;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:16
 * Package name : org.eson.liteble.activity.fragment
 * Des :
 */
public class SendDataFragment extends BaseObserveFragment {
    private ActivitySendDataBinding sendDataBinding;

    private String serviceUUID;
    private String characterUUID;
    private String connectMac;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        sendDataBinding = ActivitySendDataBinding.inflate(inflater, container, false);
        return sendDataBinding.getRoot();
    }

    @Override
    protected void initListener() {
        sendDataBinding.sendBtn.setOnClickListener(v -> sendData());
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args == null) {
            return;
        }
        serviceUUID = args.getString("serviceUUID");
        characterUUID = args.getString("characterUUID");
        connectMac = args.getString("address");
    }


    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {

    }


    private void sendData() {
        String data = sendDataBinding.editText.getText().toString();
        if (TextUtils.isEmpty(data)) {
            return;
        }

        if (data.length() % 2 != 0) {
            return;
        }

        byte[] buffer = BLEByteUtil.hexStringToByte(data);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setAddress(connectMac);
        sendMessage.setServiceUUID(UUID.fromString(serviceUUID));
        sendMessage.setCharacteristicUUID(UUID.fromString(characterUUID));
        sendMessage.setBytes(buffer);


        TMessage tMessage = new TMessage();
        tMessage.setBytes(buffer);

        tMessage.setObject(sendMessage);

        Command command = Dispatcher.getInstance().getApi();

        SenderCall<String> senderCall = command.sendCmd(tMessage);
        senderCall.execute(new OnMsgSendListener<String>() {
            @Override
            public void onTimeout() {

                LogUtil.e("send data time out ");
            }

            @Override
            public void onDataReceived(String s, TMessage tMessage) {

            }
        });
    }

}
