package org.eson.liteble.activity.fragment;

import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavArgument;
import androidx.navigation.fragment.NavHostFragment;

import com.e.ble.control.BLEControl;
import com.e.ble.core.BleTool;
import com.e.ble.core.bean.ConnectBt;
import com.e.ble.util.BLEConstant;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.activity.adapter.DeviceDetailAdapter;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.activity.vms.ServiceListViewModel;
import org.eson.liteble.ble.BleService;
import org.eson.liteble.ble.bean.ServiceBean;
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
    private ConnectBt connectBt;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        detailBinding = ActivityDetailBinding.inflate(inflater, container, false);
        return detailBinding.getRoot();
    }


    @Override
    protected void onProcess() {
        super.onProcess();
        serviceListViewModel = getDefaultViewModelProviderFactory().create(ServiceListViewModel.class);

        Map<String, NavArgument> map = NavHostFragment.findNavController(this).getGraph().getArguments();
        NavArgument navArgument = map.get("connectBt");
        if (navArgument == null){
            return;
        }
        connectBt = (ConnectBt) navArgument.getDefaultValue();
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
                BLEControl.get().disconnect(connectBt.getAddress());
                isConnect = false;
            } else {
                BleService.get().connectionDevice(connectBt.getAddress());
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
        switch (currentState) {

            case BLEConstant.State.STATE_DIS_CONNECTED:
                isConnect = false;
                detailBinding.disconnect.setText("重新连接设备");


                mDeviceDetailAdapter.setDataList(new ArrayList<>());
                mDeviceDetailAdapter.notifyDataSetChanged();
                break;
            case BLEConstant.Connection.STATE_CONNECT_SUCCEED:
            case BLEConstant.Connection.STATE_CONNECT_CONNECTED:
                isConnect = true;
                detailBinding.disconnect.setText("断开连接");
                getMessage();
                break;
            default:
                break;
        }
    }

    /**
     * 获取设备的服务和特性详情
     */
    private void getMessage() {

//        String connectMac = MyApplication.getInstance().getCurrentShowDevice();

       ;
        BluetoothGatt gatt =  BleTool.getInstance().getController().getGatt(connectBt.getAddress());
        if (gatt == null) {
            return;
        }

        if (serviceListViewModel== null){
            return;
        }
        serviceListViewModel.getServiceList(gatt)
                .observe(this, this::setAdapter);

    }

    /**
     * @param serviceBeanList
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
            bundle.putString("address",connectBt.getAddress());
            navigateNext(R.id.action_serviceListFragment_to_serviceInfoFragment, bundle);
        });
        detailBinding.detailList.setAdapter(mDeviceDetailAdapter);

    }


    private Thread mThread;

    private void startReadTimer() {
        final String connectMac = MyApplication.getInstance().getCurrentShowDevice();
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (isReadStart) {

                        if (TextUtils.isEmpty(connectMac)) {
                            break;
                        }
                        BLEControl.get().readGattRssi(connectMac);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
