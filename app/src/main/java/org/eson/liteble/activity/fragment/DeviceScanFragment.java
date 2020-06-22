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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.ble.bean.BLEDevice;
import com.e.ble.scan.BLEScanner;
import com.e.ble.util.BLEConstant;

import org.eson.liteble.MyApplication;
import org.eson.liteble.activity.DeviceActivity;
import org.eson.liteble.activity.MainActivity;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.activity.vms.ScannerViewModel;
import org.eson.liteble.activity.vms.data.ScanLiveData;
import org.eson.liteble.adapter.ScanBLEItem;
import org.eson.liteble.databinding.FragmentScanDeviceBinding;
import org.eson.liteble.service.BleService;
import org.eson.liteble.share.ConfigShare;
import org.eson.liteble.util.BondedDeviceBean;
import org.eson.liteble.util.BondedDeviceUtil;
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

public class DeviceScanFragment extends BaseObserveFragment {

    private ScannerViewModel scannerViewModel;
    private CommonRcvAdapter<BLEDevice> scanBLEAdapter;
    private ProgressDialog m_pDialog;

    private BLEDevice selectDevice = null;

    private boolean isFilterNoName;
    private int scanTime;

    FragmentScanDeviceBinding scanDeviceBinding;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        scanDeviceBinding = FragmentScanDeviceBinding.inflate(inflater, container, false);
        return scanDeviceBinding.getRoot();
    }

    @Override
    protected void initListener() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        scanDeviceBinding.listview.setLayoutManager(layoutManager);
        scanBLEAdapter = new CommonRcvAdapter<BLEDevice>(new ArrayList<>()) {
            @NonNull
            @Override
            public AdapterItem createItem(Object o) {
                return new ScanBLEItem(getActivity(), mOnClickListener);
            }
        };
        scanDeviceBinding.listview.setAdapter(scanBLEAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ConfigShare configShare = new ConfigShare(getActivity());
        isFilterNoName = configShare.getFilterNoName();
        scanTime = configShare.getConnectTime();
        scannerViewModel = getDefaultViewModelProviderFactory().create(ScannerViewModel.class);
    }

    @Override
    public void onPause() {
        BLEScanner.get().stopScan();
        super.onPause();
    }

    private ScanBLEItem.ItemClickListener mOnClickListener = device -> {
        selectDevice = device;

        BleService.get().connectionDevice(getActivity(), selectDevice.getMac());

        showProgress("正在连接设备：" + selectDevice.getName());
    };


    /**
     * 显示等待框
     *
     * @param msg
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

        if (m_pDialog == null) {
            return;
        }
        if (getActivity() == null) {
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
                ((MainActivity) getActivity()).reSetMenu();
                scanLiveData.removeObservers(this);
                return;
            }
            scanBLEAdapter.setData(liveData.getDeviceList());
            scanBLEAdapter.notifyDataSetChanged();
        });
    }
    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {

        LogUtil.e("DeviceScanFragment onDeviceStateChange : " + deviceMac);
        onBleStateChange(deviceMac, currentState);
    }


    public void onBleStateChange(String mac, int state) {

        switch (state) {
            case BLEConstant.Connection.STATE_CONNECT_CONNECTED:
            case BLEConstant.Connection.STATE_CONNECT_SUCCEED:

                BondedDeviceUtil.get().addBondDevice(mac);
                BondedDeviceBean bondedDeviceBean = BondedDeviceUtil.get().getDevice(mac);
                if (mac.equals(selectDevice.getMac())) {

                    bondedDeviceBean.setName(selectDevice.getName());
                }
                bondedDeviceBean.setConnected(true);

                MyApplication.getInstance().setCurrentShowDevice(selectDevice.getMac());
                startToNext();
                break;
            case BLEConstant.Connection.STATE_CONNECT_FAILED:
                hideProgress();
                ToastUtil.showShort(getActivity(), "设备连接失败");
                break;
            case BLEConstant.State.STATE_CONNECTED:
                hideProgress();
                ToastUtil.showShort(getActivity(), "设备连接成功");

                break;
            case BLEConstant.State.STATE_DIS_CONNECTED:
                hideProgress();
                ToastUtil.showShort(getActivity(), "设备断开");
                break;
            default:
                break;

        }
    }

    /**
     * 跳转的详情界面
     */
    private void startToNext() {
//        if (!MyApplication.getInstance().isForeground(MainActivity.class.getName())) {
//            return;
//        }
        hideProgress();


        ToastUtil.showShort(getActivity(), "连接成功");
//        Intent intent = new Intent(getActivity(), BleDetailActivity.class);
//        intent.putExtra("mac", selectDevice.getMac());
//        intent.putExtra("name", selectDevice.getName());
//        startActivity(intent);

        startActivity(new Intent(getActivity(), DeviceActivity.class));
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
