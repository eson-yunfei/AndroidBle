package org.eson.liteble.activity.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.e.ble.util.BLEByteUtil;

import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.databinding.ActivitySendDataBinding;
import org.eson.liteble.ble.BleService;

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


        BleService.get().sendData(UUID.fromString(serviceUUID), UUID.fromString(characterUUID), buffer);
    }

}
