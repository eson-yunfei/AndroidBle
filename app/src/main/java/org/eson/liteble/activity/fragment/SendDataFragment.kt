package org.eson.liteble.activity.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.shon.bluetooth.core.call.WriteCall;
import com.shon.bluetooth.core.callback.WriteCallback;
import com.shon.bluetooth.util.ByteUtil;

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

        viewBinding.sendBtn.setOnClickListener(v -> sendData());
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
        String data = viewBinding.editText.getText().toString();
        if (TextUtils.isEmpty(data)) {
            return;
        }

        if (data.length() % 2 != 0) {
            return;
        }

        final byte[] buffer = ByteUtil.hexStringToByte(data);

        new WriteCall(connectMac)
                .setServiceUUid(serviceUUID)
                .setCharacteristicUUID(characterUUID)
                .enqueue(new WriteCallback(connectMac) {
                    @Override
                    public byte[] getSendData() {
                        return buffer;
                    }

                    @Override
                    public boolean process(String address, byte[] result) {
                        return false;
                    }

                    @Override
                    public boolean removeOnWriteSuccess() {
                        return true;
                    }

                    @Override
                    public void onTimeout() {

                    }
                });
    }

}
