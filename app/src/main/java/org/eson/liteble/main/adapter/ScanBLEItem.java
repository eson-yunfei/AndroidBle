package org.eson.liteble.main.adapter;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;


import org.eson.liteble.R;
import org.eson.liteble.common.util.ByteUtil;

import kale.adapter.item.AdapterItem;
import no.nordicsemi.android.support.v18.scanner.ScanRecord;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

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
public class ScanBLEItem implements AdapterItem<ScanResult> {
    private final ItemClickListener mOnClickListener;
    private View rootView;
    private TextView deviceName;
    private TextView deviceMac;
    private TextView scanRet;
    private TextView deviceRssi;

    public ScanBLEItem(ItemClickListener clickListener) {
        mOnClickListener = clickListener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_scan_device;
    }

    @Override
    public void bindViews(View view) {
        rootView = view;
        this.scanRet = view.findViewById(R.id.scanRet);
        this.deviceName = view.findViewById(R.id.deviceName);
        this.deviceMac = view.findViewById(R.id.deviceMac);
        this.deviceRssi = view.findViewById(R.id.deviceRssi);
    }

    @Override
    public void setViews() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void handleData(ScanResult device, final int position) {

        String name = device.getDevice().getName();
        String mac = device.getDevice().getAddress();

        deviceRssi.setText("RSSI：" + device.getRssi());
        deviceName.setText(name);
        deviceMac.setText(mac);

        scanRet.setVisibility(View.INVISIBLE);

        rootView.setOnClickListener(v -> mOnClickListener.onItemClick(device));


        ScanRecord scanRecord = device.getScanRecord();
        if (scanRecord == null) {
            return;
        }
        SparseArray<byte[]> array = scanRecord.getManufacturerSpecificData();
        if (array == null || array.size() == 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        scanRet.setVisibility(View.VISIBLE);
        for (int i = 0; i < array.size(); i++) {
            int key = array.keyAt(0);
            byte[] b = (byte[]) array.get(key);

            builder.append(ByteUtil.getFormatHexString(b));
            if (i != array.size() - 1) {
                builder.append("\n");
            }
        }
        scanRet.setText(builder.toString());


    }

    public interface ItemClickListener {
        void onItemClick(ScanResult bleDevice);
    }
}
