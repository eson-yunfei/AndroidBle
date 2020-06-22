package org.eson.liteble.activity.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.e.ble.bean.BLEDevice;
import com.e.ble.support.ScanRecord;
import com.e.ble.util.BLEByteUtil;

import org.eson.liteble.R;

import kale.adapter.item.AdapterItem;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity.adapter
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 15:40
 * @change
 * @chang time
 * @class describe
 */
public class ScanBLEItem implements AdapterItem<BLEDevice> {
    private ItemClickListener mOnClickListener;
    private View rootView;
    private TextView deviceName;
    private TextView deviceMac;
    private TextView scanRet;
    private TextView deviceRssi;

    public ScanBLEItem(Context context, ItemClickListener clickListener) {
        mOnClickListener = clickListener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_scan_device;
    }

    @Override
    public void bindViews(View view) {
        rootView = view;
        this.scanRet = (TextView) view.findViewById(R.id.scanRet);
        this.deviceName = (TextView) view.findViewById(R.id.deviceName);
        this.deviceMac = (TextView) view.findViewById(R.id.deviceMac);
        this.deviceRssi = (TextView) view.findViewById(R.id.deviceRssi);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void handleData(BLEDevice device, final int position) {

        String name = device.getName();
        String mac = device.getMac();

        deviceRssi.setText("RSSI：" + device.getRssi());
        deviceName.setText(name);
        deviceMac.setText(mac);

        scanRet.setVisibility(View.INVISIBLE);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onItemClick(device);
            }
        });


        ScanRecord scanRecord = device.getScanRecord();
        if (scanRecord == null) {
            return;
        }
        SparseArray array = scanRecord.getManufacturerSpecificData();
        if (array == null || array.size() == 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        scanRet.setVisibility(View.VISIBLE);
        for (int i = 0; i < array.size(); i++) {
            int key = array.keyAt(0);
            byte[] b = (byte[]) array.get(key);

            builder.append(BLEByteUtil.getHexString(b));
            if (i != array.size() - 1) {
                builder.append("\n");
            }
        }
        scanRet.setText(builder.toString());


    }

    public interface ItemClickListener {
        void onItemClick(BLEDevice bleDevice);
    }
}
