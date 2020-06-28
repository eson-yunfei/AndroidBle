package org.eson.liteble.activity.fragment;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavArgument;
import androidx.navigation.fragment.NavHostFragment;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.state.ConnectResult;

import org.eson.liteble.R;
import org.eson.liteble.activity.adapter.DeviceDetailAdapter;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.activity.bean.ServiceBean;
import org.eson.liteble.activity.vms.ConnectViewModel;
import org.eson.liteble.activity.vms.ServiceListViewModel;
import org.eson.liteble.databinding.ActivityDetailBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:12
 * Package name : org.eson.liteble.activity.fragment
 * Des : 支持服务列表界面
 */
public class ServiceListFragment extends BaseObserveFragment {

    private ActivityDetailBinding detailBinding;

    private boolean isConnect = true;

    private boolean isReadStart = false;
    private DeviceDetailAdapter mDeviceDetailAdapter;

    private ServiceListViewModel serviceListViewModel;
    private ConnectViewModel connectViewModel;
    private ConnectResult connectBt;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        detailBinding = ActivityDetailBinding.inflate(inflater, container, false);
        return detailBinding.getRoot();
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
        detailBinding.name.setText(devName);
        getMessage();
    }


    @Override
    protected void initListener() {

        detailBinding.disconnect.setOnClickListener(v -> {
            if (isConnect) {
                connectViewModel.disConnect(connectBt.getAddress());
                isConnect = false;
            } else {
                connectViewModel.connectDevice(connectBt.getAddress())
                .observe(this, connectDeviceData -> {

                    if (connectDeviceData == null){
                        return;
                    }
                   ConnectResult connectResult =  connectDeviceData.getConnectBt();
                    if (connectResult != null){
                        getMessage();
                    }
                });
            }
        });

        detailBinding.readRssiBtn.setOnClickListener(v -> {

            if (isReadStart) {

                isReadStart = false;
                detailBinding.readRssiBtn.setText("开始读取信号值");
                stopReadTimer();

            } else {
                isReadStart = true;
                createFile();
                detailBinding.readRssiBtn.setText("停止读取信号值");
                startReadTimer();
            }
        });

    }

    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {

        if (currentState == BluetoothProfile.STATE_DISCONNECTED) {
            isConnect = false;
            detailBinding.disconnect.setText("重新连接设备");
            mDeviceDetailAdapter.setDataList(new ArrayList<>());
            mDeviceDetailAdapter.notifyDataSetChanged();
        }
        if (currentState == BluetoothProfile.STATE_CONNECTED) {
            isConnect = true;
            detailBinding.disconnect.setText("断开连接");
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
        detailBinding.detailList.setAdapter(mDeviceDetailAdapter);

    }


    private Thread mThread;

    private void startReadTimer() {
        if (mThread == null) {
            mThread = new Thread(() -> {

                while (isReadStart) {

                    if (TextUtils.isEmpty(connectBt.getAddress())) {
                        break;
                    }
//                        BLEControl.get().readGattRssi(connectBt.getAddress());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            mThread.start();
        }
    }

    private void stopReadTimer() {
        try {
            mThread.interrupt();
            mThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createFile() {
        String sd = Environment.getExternalStorageDirectory().getPath() + "/LiteBle/rssi";
        String fileName = sd + "/rssi.csv";
        File file = new File(sd);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
