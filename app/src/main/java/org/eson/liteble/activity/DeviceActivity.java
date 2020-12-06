package org.eson.liteble.activity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.shon.mvvm.base.ui.BaseBindingActivity;

import org.eson.liteble.activity.vms.DeviceControlViewModel;
import org.eson.liteble.databinding.ActivityDeviceBinding;
import org.jetbrains.annotations.Nullable;

public class DeviceActivity extends BaseBindingActivity<ActivityDeviceBinding> {

    private DeviceControlViewModel deviceControlViewModel;
    private String connectDeviceAddress;


    @Override
    public void onProcess(@Nullable Bundle bundle) {
        deviceControlViewModel = ViewModelProviders.of(this).get(DeviceControlViewModel.class);

        connectDeviceAddress = (String) getIntent().getStringExtra("connectBt");
        deviceControlViewModel.setConnectDevice(connectDeviceAddress);
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.connected_device_view);
//
//        if (navHostFragment == null) {
//            return;
//        }
//        NavController controller = navHostFragment.getNavController();
//        NavGraph graph = controller.getGraph();
//        NavArgument argument = new NavArgument.Builder()
////                .setDefaultValue(connectBt)
//                .build();
//        graph.addArgument("connectBt", argument);
//
//        controller.setGraph(graph);
    }


}