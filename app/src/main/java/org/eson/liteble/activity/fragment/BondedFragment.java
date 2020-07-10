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
import android.view.ViewGroup;

import androidx.lifecycle.Observer;

import com.e.ble.scan.BLEScanner;
import com.e.ble.util.BLEConstant;

import org.eson.liteble.LittleBleViewModel;
import org.eson.liteble.activity.DeviceActivity;
import org.eson.liteble.activity.adapter.BondedDevAdapter;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.activity.bean.BondedDeviceBean;
import org.eson.liteble.databinding.FragmentBondedDeviceBinding;
import org.eson.liteble.util.ToastUtil;

import java.util.List;

/**
 * @package_name org.eson.liteble.activity.fragment
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/5.
 * @description
 */

public class BondedFragment extends BaseObserveFragment<FragmentBondedDeviceBinding> {

    private ProgressDialog m_pDialog;
    private List<BondedDeviceBean> mDeviceBeanList;

    private BondedDeviceBean selectDevice = null;



    @Override
    protected FragmentBondedDeviceBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBondedDeviceBinding.inflate(inflater,container,false);
    }

    @Override
    protected void initListener() {
        viewBinding.bondedDeviceList.setOnItemClickListener((parent, view, position, id) -> {

            selectDevice = mDeviceBeanList.get(position);
            showProgress("正在连接设备：" + selectDevice.getName());
//            MyApplication.getInstance().setCurrentShowDevice(selectDevice.getAddress());
            BLEScanner.get().stopScan();
//            BleService.get().connectionDevice(selectDevice.getAddress());
        });
    }


    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {
        onBleStateChange(deviceMac, currentState);
    }


    @Override
    public void onResume() {
        super.onResume();
        LittleBleViewModel.getViewModel()
                .getBondList()
                .observerBondList().observe(this,
                new Observer<List<BondedDeviceBean>>() {
                    @Override
                    public void onChanged(List<BondedDeviceBean> bondedDeviceBeans) {
                        mDeviceBeanList = bondedDeviceBeans;
                        BondedDevAdapter mBondedDevAdapter = new BondedDevAdapter(getActivity(), mDeviceBeanList);
                        viewBinding.bondedDeviceList.setAdapter(mBondedDevAdapter);
                    }
                });


    }


    public void onBleStateChange(String mac, int state) {

        switch (state) {
            case BLEConstant.Connection.STATE_CONNECT_CONNECTED:
            case BLEConstant.Connection.STATE_CONNECT_SUCCEED:

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
        hideProgress();

        ToastUtil.showShort(getActivity(), "连接成功");

        startActivity(new Intent(getActivity(), DeviceActivity.class));
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
        requireActivity().runOnUiThread(() -> m_pDialog.show());

    }

    public void hideProgress() {

        if (m_pDialog == null) {
            return;
        }
        requireActivity().runOnUiThread(() -> m_pDialog.dismiss());

    }


}
