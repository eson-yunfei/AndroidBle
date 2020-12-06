package org.eson.liteble.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.databinding.ActivitySendDataBinding;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:16
 * Package name : org.eson.liteble.activity.fragment
 * Des :
 */
public class SendDataFragment extends BaseObserveFragment<ActivitySendDataBinding> {

    private String serviceUUID;
    private String characterUUID;
    private String connectMac;

    @Override
    protected ActivitySendDataBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ActivitySendDataBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initListener() {

//        viewBinding.sendBtn.setOnClickListener(v -> sendData());
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


//    private void sendData() {
//        String data = viewBinding.editText.getText().toString();
//        if (TextUtils.isEmpty(data)) {
//            return;
//        }
//
//        if (data.length() % 2 != 0) {
//            return;
//        }
//
//        byte[] buffer = BLEByteUtil.hexStringToByte(data);
//
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setAddress(connectMac);
//        sendMessage.setServiceUUID(UUID.fromString(serviceUUID));
//        sendMessage.setCharacteristicUUID(UUID.fromString(characterUUID));
//        sendMessage.setBytes(buffer);
//
//
//        TMessage tMessage = new TMessage();
//        tMessage.setBytes(buffer);
//
//        tMessage.setObject(sendMessage);
//
//        Command command = Dispatcher.getInstance().getApi();
//
//        SenderCall<String> senderCall = command.sendCmd(tMessage);
//        senderCall.execute(new OnMsgSendListener<String>() {
//            @Override
//            public void onTimeout() {
//
//                LogUtil.e("send data time out ");
//            }
//
//            @Override
//            public void onDataReceived(String s, TMessage tMessage) {
//
//            }
//        });
//    }

}
