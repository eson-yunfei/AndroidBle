package org.eson.liteble.activity;

import android.view.View;

import org.eson.liteble.activity.base.ViewBindActivity;
import org.eson.liteble.databinding.ActivityDeviceBinding;

public class DeviceActivity extends ViewBindActivity {

    private ActivityDeviceBinding deviceBinding;



    @Override
    protected View getBindViewRoot() {
        deviceBinding = ActivityDeviceBinding.inflate(getLayoutInflater());
        return deviceBinding.getRoot();
    }

    @Override
    protected void onProcess() {

    }
}