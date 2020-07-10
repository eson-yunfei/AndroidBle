package org.eson.liteble.activity.fragment;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.navigation.NavArgument;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.state.ConnectResult;

import org.eson.liteble.R;
import org.eson.liteble.activity.adapter.DeviceDetailAdapter;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.activity.bean.ServiceBean;
import org.eson.liteble.activity.vms.ConnectViewModel;
import org.eson.liteble.activity.vms.ServiceListViewModel;
import org.eson.liteble.databinding.ActivityDetailBinding;
import org.eson.liteble.task.ReadRssiTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:12
 * Package name : org.eson.liteble.activity.fragment
 * Des : 支持服务列表界面
 */
public class ServiceListFragment extends BaseObserveFragment<ActivityDetailBinding> {

    private boolean isConnect = true;

    private boolean isReadStart = false;
    private DeviceDetailAdapter mDeviceDetailAdapter;

    private ServiceListViewModel serviceListViewModel;
    private ConnectViewModel connectViewModel;
    private ConnectResult connectBt;

    @Override
    protected ActivityDetailBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ActivityDetailBinding.inflate(inflater, container, false);
    }


    @Override
    protected void onProcess() {
        super.onProcess();
        serviceListViewModel = getDefaultViewModelProviderFactory().create(ServiceListViewModel.class);
        connectViewModel = getDefaultViewModelProviderFactory().create(ConnectViewModel.class);
        Map<String, NavArgument> map = NavHostFragment.findNavController(this).getGraph().getArguments();
        NavArgument navArgument = map.get("connectBt");
        if (navArgument == null) {
            return;
        }
        connectBt = (ConnectResult) navArgument.getDefaultValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        String devName = connectBt.getName();
        viewBinding.name.setText(devName);
        getMessage();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopReadTimer();
    }


    @Override
    protected void initListener() {

        viewBinding.disconnect.setOnClickListener(v -> {
            if (isConnect) {
                connectViewModel.disConnect(connectBt.getAddress());
                isConnect = false;
            } else {
                connectViewModel.connectDevice(connectBt.getAddress())
                        .observe(this, connectDeviceData -> {

                            if (connectDeviceData == null) {
                                return;
                            }
                            ConnectResult connectResult = connectDeviceData.getConnectBt();
                            if (connectResult != null) {
                                getMessage();
                            }
                        });
            }
        });

        viewBinding.readRssiBtn.setOnClickListener(v -> {

            if (isReadStart) {

                isReadStart = false;
                viewBinding.readRssiBtn.setText("开始读取信号值");
                stopReadTimer();

            } else {
                isReadStart = true;

                viewBinding.readRssiBtn.setText("停止读取信号值");
                startReadTimer();
            }
        });

    }

    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {

        if (currentState == BluetoothProfile.STATE_DISCONNECTED) {
            isConnect = false;
            viewBinding.disconnect.setText("重新连接设备");
            mDeviceDetailAdapter.setDataList(new ArrayList<>());
            mDeviceDetailAdapter.notifyDataSetChanged();
        }
        if (currentState == BluetoothProfile.STATE_CONNECTED) {
            isConnect = true;
            viewBinding.disconnect.setText("断开连接");
            getMessage();
        }

    }

    /**
     * 获取设备的服务和特性详情
     */
    private void getMessage() {

        BluetoothGatt gatt = BleTool.getInstance().getController().getGatt(connectBt.getAddress());
        if (gatt == null) {
            return;
        }

        if (serviceListViewModel == null) {
            return;
        }
        serviceListViewModel.getServiceList(gatt)
                .observe(this, this::setAdapter);

    }

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
            bundle.putString("address", connectBt.getAddress());
            navigateNext(R.id.action_serviceListFragment_to_serviceInfoFragment, bundle);
        });
        viewBinding.detailList.setAdapter(mDeviceDetailAdapter);

    }

    private ReadRssiTask readRssiTask;
    private void startReadTimer() {
        if (readRssiTask != null) {
            return;
        }
        readRssiTask = new ReadRssiTask(connectBt.getAddress());
        readRssiTask.start();
    }

    private void stopReadTimer() {
        if (readRssiTask != null) {
            readRssiTask.stopRead();
            readRssiTask = null;
        }
    }


}
