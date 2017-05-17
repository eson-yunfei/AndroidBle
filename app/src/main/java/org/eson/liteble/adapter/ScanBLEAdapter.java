package org.eson.liteble.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.ble.bean.BLEDevice;
import com.e.ble.support.ScanRecord;
import com.e.ble.util.BLEByteUtil;

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
            view = inflater.inflate(org.eson.liteble.R.layout.item_scan_device, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BLEDevice device = dataList.get(position);
        String name = device.getName();
//		LogUtil.d("信号:" + device.getRssi());
//		LogUtil.e("name = " + name);
        String mac = device.getMac();

        viewHolder.deviceRssi.setText("RSSI：" + device.getRssi());
        viewHolder.deviceName.setText(name);
        viewHolder.deviceMac.setText(mac);

        viewHolder.scanRet.setVisibility(View.GONE);
        ScanRecord scanRecord = device.getScanRecord();
        SparseArray array = scanRecord.getManufacturerSpecificData();
        if (array == null || array.size() == 0) {
            return view;
        }

        StringBuilder builder = new StringBuilder();
        viewHolder.scanRet.setVisibility(View.VISIBLE);
        for (int i = 0; i < array.size(); i++) {
            int key = array.keyAt(0);
            byte[] b = (byte[]) array.get(key);

            builder.append(BLEByteUtil.getHexString(b));
            if (i != array.size() - 1) {
                builder.append("\n");
            }
        }
        viewHolder.scanRet.setText(builder.toString());
        return view;
    }

    class ViewHolder {
        View rootView;

        TextView deviceName;
        TextView deviceMac;
        TextView scanRet;
        TextView deviceRssi;

        ViewHolder(View rootView) {

            this.rootView = rootView;
            this.scanRet = findView(rootView, R.id.scanRet);
            this.deviceName = findView(rootView, R.id.deviceName);
            this.deviceMac = findView(rootView, R.id.deviceMac);
            this.deviceRssi = findView(rootView, R.id.deviceRssi);
        }
    }
}
