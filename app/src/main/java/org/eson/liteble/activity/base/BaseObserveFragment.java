package org.eson.liteble.activity.base;

import androidx.viewbinding.ViewBinding;

import org.eson.liteble.LittleBleViewModel;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 16:40
 * Package name : org.eson.liteble.activity.base
 * Des :
 */
public abstract class BaseObserveFragment<VB extends ViewBinding> extends ViewBindFragment<VB> implements IObserve {

    private LittleBleViewModel.DeviceState deviceStateLiveData;

    @Override
    public void onResume() {
        super.onResume();
        observerViewModel();
    }

    @Override
    public void observerViewModel() {
        LittleBleViewModel littleBleViewModel = LittleBleViewModel.getViewModel();
        deviceStateLiveData = littleBleViewModel.observerDeviceState();
        deviceStateLiveData.observe(this, deviceState -> {
            if (deviceState == null){
                return;
            }

            onDeviceStateChange(deviceState.getMac(),deviceState.getState());
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        deviceStateLiveData.removeObservers(this);
        deviceStateLiveData = null;
    }


}
