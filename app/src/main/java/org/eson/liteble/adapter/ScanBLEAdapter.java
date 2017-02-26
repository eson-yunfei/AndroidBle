package org.eson.liteble.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eson.ble_sdk.bean.BLEDevice;
import org.eson.liteble.util.LogUtil;
import org.eson.liteble.R;

import java.util.List;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.adapter
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 15:40
 * @change
 * @chang time
 * @class describe
 */
public class ScanBLEAdapter extends MyBaseAdapter<BLEDevice> {

	public ScanBLEAdapter(Context context, List<BLEDevice> dataList) {
		super(context, dataList);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.item_scan_device, null);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		BLEDevice device = dataList.get(position);
		String name = device.getName();
		LogUtil.d("信号:" + device.getRssi());
		LogUtil.e("name = " + name);
		String mac = device.getMac();
		viewHolder.deviceRssi.setText("信号强度：" + device.getRssi());
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
