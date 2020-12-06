package org.eson.liteble.activity.fragment;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.shon.mvvm.base.ui.BaseBindingFragment;

import org.eson.liteble.activity.adapter.DeviceDetailAdapter;
import org.eson.liteble.activity.bean.ServiceBean;
import org.eson.liteble.activity.vms.ConnectViewModel;
import org.eson.liteble.activity.vms.DeviceControlViewModel;
import org.eson.liteble.databinding.ActivityDetailBinding;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:12
 * Package name : org.eson.liteble.activity.fragment
 * Des : 支持服务列表界面
 */
public class ServiceListFragment extends BaseBindingFragment<ActivityDetailBinding> {

    private DeviceControlViewModel controlViewModel;
    private DeviceDetailAdapter mDeviceDetailAdapter;
    private ConnectViewModel connectViewModel;


    @Override
    public void initViewState() {
        super.initViewState();
    }

    @Override
    public void initViewListener() {
        super.initViewListener();
    }

    @Override
    public void onProcess(@Nullable Bundle savedInstanceState) {
        super.onProcess(savedInstanceState);
        connectViewModel = getDefaultViewModelProviderFactory().create(ConnectViewModel.class);
        controlViewModel = ViewModelProviders.of(getActivity()).get(DeviceControlViewModel.class);
        controlViewModel.getMutableLiveData().observe(this, new Observer<List<ServiceBean>>() {
            @Override
            public void onChanged(List<ServiceBean> serviceBeans) {

                setAdapter(serviceBeans);
            }
        });
    }

//    @Override
//    protected void onProcess() {
//        super.onProcess();
//
//        Map<String, NavArgument> map = NavHostFragment.findNavController(this).getGraph().getArguments();
//        NavArgument navArgument = map.get("connectBt");
//        if (navArgument == null) {
//            return;
//        }
//    }


//    @Override
//    protected void initListener() {

//        viewBinding.disconnect.setOnClickListener(v -> {
//            if (isConnect) {
//                connectViewModel.disConnect(connectBt.getAddress());
//                isConnect = false;
//            } else {
//                connectViewModel.connectDevice(connectBt.getAddress())
//                        .observe(this, connectDeviceData -> {
//
//                            if (connectDeviceData == null) {
//                                return;
//                            }
//                            ConnectResult connectResult = connectDeviceData.getConnectBt();
//                            if (connectResult != null) {
//                                getMessage();
//                            }
//                        });
//            }
//        });
//
//        viewBinding.readRssiBtn.setOnClickListener(v -> {
//
//            if (isReadStart) {
//
//                isReadStart = false;
//                viewBinding.readRssiBtn.setText("开始读取信号值");
//                stopReadTimer();
//
//            } else {
//                isReadStart = true;
//
//                viewBinding.readRssiBtn.setText("停止读取信号值");
//                startReadTimer();
//            }
//        });
//
//    }

//    @Override
//    public void onDeviceStateChange(String deviceMac, int currentState) {
//
//        if (currentState == BluetoothProfile.STATE_DISCONNECTED) {
//            isConnect = false;
//            viewBinding.disconnect.setText("重新连接设备");
////            mDeviceDetailAdapter.setDataList(new ArrayList<>());
////            mDeviceDetailAdapter.notifyDataSetChanged();
//        }
//        if (currentState == BluetoothProfile.STATE_CONNECTED) {
//            isConnect = true;
//            viewBinding.disconnect.setText("断开连接");
//            getMessage();
//        }
//
//    }


    /**
     * @param serviceBeanList serviceBeanList
     */
    private void setAdapter(List<ServiceBean> serviceBeanList) {
        if (serviceBeanList == null) {
            serviceBeanList = new ArrayList<>();
        }
        mDeviceDetailAdapter = new DeviceDetailAdapter(getActivity(), serviceBeanList);
        mDeviceDetailAdapter.setOnItemClickListener((serviceBean, position) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("serviceBean", serviceBean);
            bundle.putInt("position", position);
//            bundle.putString("address", connectBt.getAddress());
//            navigateNext(R.id.action_serviceListFragment_to_serviceInfoFragment, bundle);
        });
        binding.detailList.setAdapter(mDeviceDetailAdapter);

    }


}
