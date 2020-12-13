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

package org.eson.liteble.main.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shon.mvvm.base.ui.BaseBindingFragment;

import org.eson.liteble.common.DeviceState;
import org.eson.liteble.databinding.FragmentBondedDeviceBinding;
import org.eson.liteble.main.adapter.BondItem;

import java.util.List;

import kale.adapter.CommonAdapter;
import kale.adapter.item.AdapterItem;

/**
 * @package_name org.eson.liteble.activity.fragment
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/5.
 * @description
 */

public class BondedFragment extends BaseBindingFragment<FragmentBondedDeviceBinding> {

    private ProgressDialog m_pDialog;


    @Override
    public void initViewListener() {
        binding.bondedDeviceList.setOnItemClickListener((parent, view, position, id) -> {

        });
    }


    @Override
    public void onProcess(@Nullable Bundle savedInstanceState) {
        super.onProcess(savedInstanceState);
        List<DeviceState.DeviceLiveData> connectDevice = DeviceState.Companion.getConnectDevice(DeviceState.Companion.getInstance());
        CommonAdapter<DeviceState.DeviceLiveData> deviceLiveDataCommonAdapter  = new CommonAdapter<DeviceState.DeviceLiveData>(connectDevice,1) {
            @NonNull
            @Override
            public AdapterItem<DeviceState.DeviceLiveData> createItem(Object type) {
                return new BondItem();
            }
        };
        binding.bondedDeviceList.setAdapter(deviceLiveDataCommonAdapter);
    }

    /**
     * 显示等待框
     *
     * @param msg
     */
    private void showProgress(String msg) {
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

    private void hideProgress() {

        if (m_pDialog == null) {
            return;
        }
        requireActivity().runOnUiThread(() -> m_pDialog.dismiss());

    }


}
