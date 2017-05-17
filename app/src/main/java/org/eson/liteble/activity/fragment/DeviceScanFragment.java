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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.e.ble.bean.BLEDevice;
import com.e.ble.scan.BLEScanCfg;
import com.e.ble.scan.BLEScanListener;
import com.e.ble.scan.BLEScanner;
import com.e.ble.util.BLEConstant;
import com.e.ble.util.BLEError;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.activity.BleDetailActivity;
import org.eson.liteble.activity.MainActivity;
import org.eson.liteble.adapter.ScanBLEAdapter;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.BondedDeviceBean;
import org.eson.liteble.util.BondedDeviceUtil;
import org.eson.liteble.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @package_name org.eson.liteble.activity.fragment
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/5.
 * @description
 */

public class DeviceScanFragment extends BaseFragment {
    private ListView mListView;

    private List<BLEDevice> deviceList = new ArrayList<>();
    private ScanBLEAdapter scanBLEAdapter;
    private ProgressDialog m_pDialog;

    private BLEDevice selectDevice = null;


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_scan_device;
    }

    @Override
    protected void initViews() {

        mListView = findView(R.id.listview);

        scanBLEAdapter = new ScanBLEAdapter(getActivity(), deviceList);
        mListView.setAdapter(scanBLEAdapter);
    }

    @Override
    protected void initViewsListener() {
        super.initViewsListener();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                selectDevice = deviceList.get(i);
                showProgress("正在连接设备：" + selectDevice.getName());

                MyApplication.getInstance().setCurrentShowDevice(selectDevice.getMac());
                BLEScanner.get().stopScan();
                BleService.get().connectionDevice(getActivity(), selectDevice.getMac());

            }
        });
    }

    @Override
    public void onPause() {
        BLEScanner.get().stopScan();
        super.onPause();
    }

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_pDialog.show();
            }
        });

    }

    public void hideProgress() {

        if (m_pDialog == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_pDialog.dismiss();
            }
        });

    }

    /**
     * 扫描蓝牙设备
     */
    private void searchDevice() {
        showProgress("搜索设备中。。。。");
        BLEScanCfg scanCfg = new BLEScanCfg.ScanCfgBuilder(
                MyApplication.getInstance().getConfigShare().getConnectTime())
                .builder();
        BLEScanner.get().startScanner(scanCfg, new BLEScanListener() {
            @Override
            public void onScannerStart() {

            }

            @Override
            public void onScanning(BLEDevice device) {
                hideProgress();
                addScanBLE(device);
            }


            @Override
            public void onScannerStop() {
                hideProgress();
                ToastUtil.showShort(getActivity(), "扫描结束");
            }

            @Override
            public void onScannerError(int errorCode) {
                hideProgress();
                if (errorCode == BLEError.BLE_CLOSE) {
                    ToastUtil.showShort(getActivity(), "蓝牙未打开，请打开蓝牙后重试");
                } else {
                    ToastUtil.showShort(getActivity(), "扫描出现异常");
                }
            }
        });
    }

    public void addScanBLE(final BLEDevice bleDevice) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isExitDevice(bleDevice)) {
                    updateDevice(bleDevice);
                } else {
                    deviceList.add(0, bleDevice);
                }
                scanBLEAdapter.notifyDataSetChanged();
            }
        });
    }


    private boolean isExitDevice(BLEDevice device) {
        synchronized (deviceList) {
            for (BLEDevice bleDevice : deviceList) {
                if (bleDevice.getMac().equals(device.getMac())) {
                    return true;
                }
            }
            return false;
        }
    }

    private void updateDevice(BLEDevice device) {
        synchronized (deviceList) {
            for (BLEDevice bleDevice : deviceList) {
                if (bleDevice.getMac().equals(device.getMac())) {
                    bleDevice.setRssi(device.getRssi());
                    bleDevice.setScanRecord(device.getScanRecord());
                }
            }
        }

    }


    @Override
    public void onBleStateChange(String mac, int state) {
        super.onBleStateChange(mac, state);

        switch (state) {
            case BLEConstant.Connection.STATE_CONNECT_CONNECTED:
            case BLEConstant.Connection.STATE_CONNECT_SUCCEED:

                BondedDeviceUtil.get().addBondDevice(mac);
                BondedDeviceBean bondedDeviceBean = BondedDeviceUtil.get().getDevice(mac);
                if (mac.equals(selectDevice.getMac())) {

                    bondedDeviceBean.setName(selectDevice.getName());
                }
                bondedDeviceBean.setConnected(true);

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

        }
    }

    /**
     * 跳转的详情界面
     */
    private void startToNext() {
        if (!MyApplication.getInstance().isForeground(MainActivity.class.getName())) {
            return;
        }
        hideProgress();

        ToastUtil.showShort(getActivity(), "连接成功");
        Intent intent = new Intent(getActivity(), BleDetailActivity.class);
        intent.putExtra("mac", selectDevice.getMac());
        intent.putExtra("name", selectDevice.getName());
        startActivity(intent);

    }

    public void stopScanner() {


        BLEScanner.get().stopScan();

    }

    public void startScanner() {
        deviceList.clear();
        scanBLEAdapter.notifyDataSetChanged();
        searchDevice();
    }
}
