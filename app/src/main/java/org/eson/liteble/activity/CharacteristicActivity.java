package org.eson.liteble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.e.ble.util.BLE_UUID_Util;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.adapter.BleDataAdapter;
import org.eson.liteble.bean.BleDataBean;
import org.eson.liteble.bean.CharacterBean;
import org.eson.liteble.bean.DescriptorBean;
import org.eson.liteble.bean.ServiceBean;
import org.eson.liteble.common.ConnectedDevice;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.UUIDFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/25
 * @说明： 蓝牙服务的特性详情页面
 */

public class CharacteristicActivity extends BaseBleActivity {

    private TextView uuid_text;            //UUID
    private TextView properties_text;    // type
    private TextView readBtn, writeBtn, notifyBtn;

    private LinearLayout descriptorLayout;

    private ListView descListView;
    private ListView dataListView;

    private CharacterBean characterBean;
    private String serviceUUID;
    private String characterUUID;
    private List<String> descriptors = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();

    private List<BleDataBean> bleDataList = new ArrayList<>();
    private BleDataAdapter adapter;
    private ArrayAdapter<String> dataListAdapter;
    private boolean isListenerNotice = false;
    private String characterName = "";

    @Override
    protected int getRootLayout() {
        return R.layout.activity_characteristic;
    }

    @Override
    protected void initView() {
        super.initView();
        uuid_text = findView(R.id.uuid_text);
        properties_text = findView(R.id.properties_text);

        readBtn = findView(R.id.readBtn);
        writeBtn = findView(R.id.writeBtn);
        notifyBtn = findView(R.id.notifyBtn);

        descriptorLayout = findView(R.id.descriptorLayout);

        descListView = findView(R.id.desc_listView);
        dataListView = findView(R.id.data_listView);
    }

    @Override
    protected void initViewListener() {
        super.initViewListener();

        readBtn.setOnClickListener(this);
        writeBtn.setOnClickListener(this);
        notifyBtn.setOnClickListener(this);

    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setData(bundle);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.writeBtn:
                Intent intent = new Intent(CharacteristicActivity.this, SendDataActivity.class);
                intent.putExtra("serviceUUID", serviceUUID);
                intent.putExtra("characterUUID", characterUUID);
                startActivity(intent);

                break;
            case R.id.readBtn:
                readCharacter();

                break;
            case R.id.notifyBtn:
                enableNotice();
                break;

        }

    }

    private void readCharacter() {
        BleService.get().readCharacter(UUID.fromString(serviceUUID),
                UUID.fromString(characterUUID));
    }

    //***************************************************************************************************//
    //***************************************************************************************************//

    @Override
    protected void changeBleData(String uuid, byte[] buffer, String deviceAddress) {

        BleDataBean bean = new BleDataBean(deviceAddress, UUID.fromString(uuid), buffer);
        bean.setTime(getCurrentTime());
        bleDataList.add(0,bean);
        if (adapter == null) {
            adapter = new BleDataAdapter(this, bleDataList, characterName);
            dataListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, SSS");
//        String currentTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
//
//        String text = BLEByteUtil.getHexString(buffer);
//
//
//        if (!characterName.equals(BLE_UUID_Util.UNKNOWN_CHARACTER)) {
//            text = text + " (  " + BLEByteUtil.byteToCharSequence(buffer) + "  )";
//        }
////
//        String showText = getString(R.string.exchange_txt_hint, currentTime, text);
//        dataList.add(0, Html.fromHtml(showText).toString());
//        if (dataListAdapter == null) {
//            dataListAdapter = new ArrayAdapter<>(CharacteristicActivity.this,
//                    R.layout.item_data, R.id.data_text, dataList);
//            dataListView.setAdapter(dataListAdapter);
//        } else {
//            dataListAdapter.notifyDataSetChanged();
//        }


    }


    //***************************************************************************************************//
    //***************************************************************************************************//

    private SimpleDateFormat simpleDateFormat =new SimpleDateFormat("HH:mm:ss, SSS", Locale.getDefault());

    private String getCurrentTime() {
        String currentTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        return currentTime;
    }

    /**
     * 启动通知服务
     */
    private void enableNotice() {

        isListenerNotice = !isListenerNotice;
        String text = isListenerNotice ? "取消监听" : "开始监听";
        notifyBtn.setText(text);
        characterBean.setListening(isListenerNotice);
        UUID des = null;
        if (descriptors.size() == 0) {
            des = UUIDFormat.DESC;
        } else {
            des = UUID.fromString(descriptors.get(0));
        }
        BleService.get().enableNotify(MyApplication.getInstance().getCurrentShowDevice(),
                UUID.fromString(serviceUUID),
                UUID.fromString(characterUUID), des, isListenerNotice);
    }

    /**
     * 发送数据
     *
     * @param bundle
     */
    private void setData(Bundle bundle) {
        int parentPosition = bundle.getInt("parentPosition");
        int position = bundle.getInt("position");
        String connectMac = MyApplication.getInstance().getCurrentShowDevice();
        ServiceBean serviceBean = ConnectedDevice.get().getServiceList(connectMac).get(parentPosition);
//        serviceUUID = serviceBean.getServiceUUID();

        characterBean = serviceBean.getUUIDBeen().get(position);
        serviceUUID = characterBean.getServiceUUID();
        characterUUID = characterBean.getCharacterUUID();

        characterName = BLE_UUID_Util.getCharacterNameByUUID(UUID.fromString(characterUUID));
        uuid_text.setText(characterUUID);


        String name = "";
        if (characterBean.isRead()) {
            name += "read ";
            readBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isWrite()) {
            name += "write ";
            writeBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isNotify()) {
            name += "notify ";
            notifyBtn.setVisibility(View.VISIBLE);
        }

        properties_text.setText(name);


        isListenerNotice = characterBean.isListening();
        String text = isListenerNotice ? "取消监听" : "开始监听";
        notifyBtn.setText(text);


        List<DescriptorBean> descriptorList = characterBean.getDescriptorBeen();
        if (descriptorList == null || descriptorList.size() == 0) {
            descriptorLayout.setVisibility(View.GONE);
            return;
        }

        for (DescriptorBean descriptorBean : descriptorList) {
            String uuid = descriptorBean.getUUID();
            descriptors.add(uuid);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CharacteristicActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, descriptors);
        descListView.setAdapter(arrayAdapter);
    }


}
