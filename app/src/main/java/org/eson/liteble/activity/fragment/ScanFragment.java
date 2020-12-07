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
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shon.mvvm.base.ui.BaseBindingFragment;

import org.eson.liteble.R;
import org.eson.liteble.activity.DeviceActivity;
import org.eson.liteble.activity.MainActivity;
import org.eson.liteble.activity.adapter.ScanBLEItem;
import org.eson.liteble.activity.vms.ConnectViewModel;
import org.eson.liteble.activity.vms.ScannerViewModel;
import org.eson.liteble.databinding.FragmentScanDeviceBinding;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

/**
 * Created by xiaoyunfei on 2017/5/5.
 *
 * update on 2020/12/06
 *
 * 扫描设备界面
 */
public class ScanFragment extends BaseBindingFragment<FragmentScanDeviceBinding>
        implements ScannerViewModel.ScannerView , ConnectViewModel.ConnectView {

    private ScannerViewModel scannerViewModel;
    private ConnectViewModel connectViewModel;
    private ProgressDialog m_pDialog;
    private CommonRcvAdapter<ScanResult> commonRcvAdapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        scannerViewModel = getDefaultViewModelProviderFactory().create(ScannerViewModel.class);
        connectViewModel = getDefaultViewModelProviderFactory().create(ConnectViewModel.class);
    }

    @Override
    public void initViewState() {

        if (binding == null) {
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.listview.setLayoutManager(layoutManager);
        commonRcvAdapter = new CommonRcvAdapter<ScanResult>(new ArrayList<>()) {
            @NonNull
            @Override
            public AdapterItem<ScanResult> createItem(Object type) {
                return new ScanBLEItem(bleDevice -> {
                    BluetoothDevice device = bleDevice.getDevice();
                    showProgress("Connecting...");
                    connectViewModel.connectDevice(device.getAddress(),device.getName());

                });
            }
        };
        binding.listview.setAdapter(commonRcvAdapter);
    }

    @Override
    public void onProcess(Bundle savedInstanceState) {

        scannerViewModel.attachView(this, this);
        connectViewModel.attachView(this,this);

    }

    @Override
    public void onFindDevices(@NonNull List<ScanResult> scanResultList) {
        hideProgress();
        commonRcvAdapter.setData(scanResultList);
        commonRcvAdapter.notifyDataSetChanged();
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



    public void stopScanner() {
        scannerViewModel.stopScanner();
    }
//
    public void startScanner() {
        showProgress(getString(R.string.text_scan_device));
        commonRcvAdapter.setData(new ArrayList<>());
        commonRcvAdapter.notifyDataSetChanged();
        scannerViewModel.startScanner();
    }

    @Override
    public void onConnected(String deviceMac, BluetoothGatt gatt) {

    }

    @Override
    public void onServerEnable(String deviceMac, BluetoothGatt gatt) {

        hideProgress();

        Intent intent = new Intent(getActivity(), DeviceActivity.class);
        intent.putExtra("connectBt", deviceMac);
        startActivity(intent);
    }

    @Override
    public void onDisconnected(String deviceMac) {

    }

    @Override
    public void connectError(String deviceMac, int errorCode) {

    }
}
