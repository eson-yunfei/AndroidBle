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

package org.eson.liteble.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eson.liteble.R;
import org.eson.liteble.util.BondedDeviceBean;

import java.util.List;

/**
 */
public class BondedDevAdapter extends MyBaseAdapter<BondedDeviceBean> {

    public BondedDevAdapter(Context context, List<BondedDeviceBean> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(org.eson.liteble.R.layout.item_scan_device, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BondedDeviceBean device = dataList.get(position);
        String name = device.getName();
//		LogUtil.d("信号:" + device.getRssi());
//		LogUtil.e("name = " + name);
        String mac = device.getAddress();
        viewHolder.deviceName.setText(name);
        viewHolder.deviceMac.setText(mac);

        return view;
    }

    class ViewHolder {
        View rootView;

        TextView deviceName;
        TextView deviceMac;
        TextView deviceRssi;

        ViewHolder(View rootView) {

            this.rootView = rootView;
            this.deviceName = findView(rootView, R.id.deviceName);
            this.deviceMac = findView(rootView, R.id.deviceMac);
            this.deviceRssi = findView(rootView, R.id.deviceRssi);
        }
    }
}
