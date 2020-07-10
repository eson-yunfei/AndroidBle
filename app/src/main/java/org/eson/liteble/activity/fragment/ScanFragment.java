/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.eson.liteble.activity.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.e.ble.bean.BLEDevice;
import com.e.ble.scan.BLEScanner;
import com.e.tool.ble.bean.state.ConnectError;
import com.e.tool.ble.bean.state.ConnectResult;
import com.e.tool.ble.bean.state.DevState;

import org.eson.liteble.activity.DeviceActivity;
import org.eson.liteble.activity.MainActivity;
import org.eson.liteble.activity.adapter.ScanBLEItem;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.activity.vms.ConnectViewModel;
import org.eson.liteble.activity.vms.ScannerViewModel;
import org.eson.liteble.activity.vms.data.ScanLiveData;
import org.eson.liteble.databinding.FragmentScanDeviceBinding;
import org.eson.liteble.share.ConfigShare;
import org.eson.liteble.util.LogUtil;
import org.eson.liteble.util.ToastUtil;

import java.util.ArrayList;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * @package_name org.eson.liteble.activity.fragment
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/5.
 * @description 扫描设备界面
 */

public class ScanFragment extends BaseObserveFragment<FragmentScanDeviceBinding> {

    private ScannerViewModel scannerViewModel;
    private ConnectViewModel connectViewModel;
    private CommonRcvAdapter<BLEDevice> scanBLEAdapter;
    private ProgressDialog m_pDialog;

    private boolean isFilterNoName;
    private int scanTime;


    @Override
    protected FragmentScanDeviceBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentScanDeviceBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initListener() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        viewBinding.listview.setLayoutManager(layoutManager);
        scanBLEAdapter = new CommonRcvAdapter<BLEDevice>(new ArrayList<>()) {
            @NonNull
            @Override
            public AdapterItem<?> createItem(Object o) {
                return new ScanBLEItem(getActivity(), mOnClickListener);
            }
        };
        viewBinding.listview.setAdapter(scanBLEAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ConfigShare configShare = new ConfigShare(getActivity());
        isFilterNoName = configShare.getFilterNoName();
        scanTime = configShare.getConnectTime();
        scannerViewModel = getDefaultViewModelProviderFactory().create(ScannerViewModel.class);
        connectViewModel = getDefaultViewModelProviderFactory().create(ConnectViewModel.class);
    }

    @Override
    public void onPause() {
        stopScanner();
        super.onPause();
    }

    private ScanBLEItem.ItemClickListener mOnClickListener = device -> {

        showProgress("正在连接设备：" + device.getName());
        connectViewModel.connectDevice(device.getMac())
                .observe(this, this::updateConnectResult);
    };

    /**
     * @param connectDeviceData connectDeviceData
     */
    private void updateConnectResult(ConnectViewModel.ConnectDeviceData connectDeviceData) {
        if (connectDeviceData == null) {
            return;
        }

        ConnectError connectError = connectDeviceData.getErrorCode();
        if (connectError != null) {
            LogUtil.e("连接设备异常 ：" + connectError.toString());
            hideProgress();
            ToastUtil.showShort(getActivity(), "设备连接失败");
            return;
        }

        DevState devState = connectDeviceData.getDevState();
        if (devState != null) {
            LogUtil.e("devState " + devState.toString());
        }

        ConnectResult connectBt = connectDeviceData.getConnectBt();
        if (connectBt != null) {
            LogUtil.e("onServicesDiscovered : address : " + connectBt.getAddress());
            hideProgress();
            ToastUtil.showShort(getActivity(), "设备连接成功");
            Intent intent = new Intent(getActivity(), DeviceActivity.class);
            intent.putExtra("connectBt", connectBt);
            startActivity(intent);
        }
    }

    /**
     * 显示等待框
     *
     * @param msg msg
     */
    public void showProgress(String msg) {
        if (m_pDialog == null) {
            m_pDialog = new ProgressDialog(getActivity());
            m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_pDialog.setIndeterminate(false);
            m_pDialog.setCancelable(true);
        }
        if (m_pDialog.isShowing()) {
            return;
        }

        m_pDialog.setMessage(msg);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            return;
        }
        mainActivity.runOnUiThread(() -> m_pDialog.show());
    }

    public void hideProgress() {
        if (m_pDialog == null || getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> m_pDialog.dismiss());

    }

    /**
     * 扫描蓝牙设备
     */
    private void searchDevice() {
        showProgress("搜索设备中。。。。");

        ScanLiveData scanLiveData = scannerViewModel.startScanDevice(isFilterNoName, scanTime);

        scanLiveData.observe(this, liveData -> {
            if (liveData == null) {
                return;
            }
            hideProgress();
            if (liveData.isStop() || liveData.isTimeout()) {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).reSetMenu();
                }
                scanLiveData.removeObservers(this);
                return;
            }
            scanBLEAdapter.setData(liveData.getDeviceList());
            scanBLEAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {
        if (currentState == BluetoothProfile.STATE_DISCONNECTED) {
            hideProgress();
            ToastUtil.showShort(getActivity(), "设备断开");
        }
    }

    public void stopScanner() {
        BLEScanner.get().stopScan();
    }

    public void startScanner() {
        scanBLEAdapter.setData(new ArrayList<>());
        scanBLEAdapter.notifyDataSetChanged();
        searchDevice();
    }
}
