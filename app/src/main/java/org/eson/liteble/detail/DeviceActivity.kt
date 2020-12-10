package org.eson.liteble.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.shon.mvvm.base.ui.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import org.eson.liteble.databinding.ActivityDeviceBinding
import org.eson.liteble.detail.viewmodel.DeviceControlViewModel

@AndroidEntryPoint
class DeviceActivity : BaseBindingActivity<ActivityDeviceBinding?>() {

    private val deviceControlViewModel: DeviceControlViewModel by viewModels()

    private var connectDeviceAddress: String? = null

    override fun onProcess(bundle: Bundle?) {
        connectDeviceAddress = intent.getStringExtra("connectBt")
        deviceControlViewModel.setConnectDevice(connectDeviceAddress)

    }
}

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.connected_device_view);
//
//        if (navHostFragment == null) {
//            return;
//        }
//        NavController controller = navHostFragment.getNavController();
//        NavGraph graph = controller.getGraph();
//        NavArgument argument = new NavArgument.Builder()
//                .setDefaultValue(connectBt)
//                .build();
//        graph.addArgument("connectBt", argument);
//
//        controller.setGraph(graph);