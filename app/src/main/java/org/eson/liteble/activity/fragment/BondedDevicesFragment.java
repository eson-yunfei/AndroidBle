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

import com.e.ble.scan.BLEScanner;
import com.e.ble.util.BLEConstant;

import org.eson.liteble.MyApplication;
import org.eson.liteble.activity.BleDetailActivity;
import org.eson.liteble.activity.MainActivity;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.adapter.BondedDevAdapter;
import org.eson.liteble.databinding.FragmentBondedDeviceBinding;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.BondedDeviceBean;
import org.eson.liteble.util.BondedDeviceUtil;
import org.eson.liteble.util.ToastUtil;

import java.util.List;

/**
 * @package_name org.eson.liteble.activity.fragment
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/5.
 * @description
 */

public class BondedDevicesFragment extends BaseObserveFragment {

    private ProgressDialog m_pDialog;
    private List<BondedDeviceBean> mDeviceBeanList;

    private BondedDeviceBean selectDevice = null;

    private FragmentBondedDeviceBinding bondedDeviceBinding;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        bondedDeviceBinding = FragmentBondedDeviceBinding.inflate(inflater, container, false);
        return bondedDeviceBinding.getRoot();
    }

    @Override
    protected void initListener() {
        bondedDeviceBinding.bondedDeviceList.setOnItemClickListener((parent, view, position, id) -> {

            selectDevice = mDeviceBeanList.get(position);
            showProgress("正在连接设备：" + selectDevice.getName());
            MyApplication.getInstance().setCurrentShowDevice(selectDevice.getAddress());
            BLEScanner.get().stopScan();
            BleService.get().connectionDevice(getActivity(), selectDevice.getAddress());
        });
    }


    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {
        onBleStateChange(deviceMac, currentState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mDeviceBeanList = BondedDeviceUtil.get().getBondedList();
        BondedDevAdapter mBondedDevAdapter = new BondedDevAdapter(getActivity(), mDeviceBeanList);
        bondedDeviceBinding.bondedDeviceList.setAdapter(mBondedDevAdapter);

    }


    public void onBleStateChange(String mac, int state) {

        switch (state) {
            case BLEConstant.Connection.STATE_CONNECT_CONNECTED:
            case BLEConstant.Connection.STATE_CONNECT_SUCCEED:

                BondedDeviceUtil.get().addBondDevice(mac);
                BondedDeviceBean bondedDeviceBean = BondedDeviceUtil.get().getDevice(mac);
                if (mac.equals(selectDevice.getAddress())) {

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

    private void startToNext() {
//        if (!MyApplication.getInstance().isForeground(MainActivity.class.getName())) {
//            return;
//        }
        hideProgress();

        ToastUtil.showShort(getActivity(), "连接成功");
        Intent intent = new Intent(getActivity(), BleDetailActivity.class);
        intent.putExtra("mac", selectDevice.getAddress());
        intent.putExtra("name", selectDevice.getName());
        startActivity(intent);
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
        requireActivity().runOnUiThread(new Runnable() {
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
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_pDialog.dismiss();
            }
        });

    }


}
