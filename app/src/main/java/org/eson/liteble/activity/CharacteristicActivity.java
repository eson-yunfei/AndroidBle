package org.eson.liteble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.bean.CharacterBean;
import org.eson.liteble.bean.DescriptorBean;
import org.eson.liteble.bean.ServiceBean;
import org.eson.liteble.common.ConnectedDevice;
import org.eson.liteble.service.BleService;

import java.util.ArrayList;
import java.util.List;
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

    private ArrayAdapter<String> dataListAdapter;
    private boolean isListenerNotice = false;

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
    protected void changeBleData(String uuid, String buffer, String deviceAddress) {
        dataList.add(0, buffer);
        if (dataListAdapter == null) {
            dataListAdapter = new ArrayAdapter<>(CharacteristicActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, dataList);
            dataListView.setAdapter(dataListAdapter);
        } else {
            dataListAdapter.notifyDataSetChanged();
        }
    }


    //***************************************************************************************************//
    //***************************************************************************************************//


    /**
     * 启动通知服务
     */
    private void enableNotice() {

        isListenerNotice = !isListenerNotice;
        String text = isListenerNotice ? "取消监听" : "开始监听";
        notifyBtn.setText(text);
        characterBean.setListening(isListenerNotice);

        BleService.get().enableNotify(MyApplication.getInstance().getCurrentShowDevice(),
                UUID.fromString(serviceUUID),
                UUID.fromString(characterUUID), UUID.fromString(descriptors.get(0)), isListenerNotice);
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
        serviceUUID = serviceBean.getServiceUUID();
        characterBean = serviceBean.getUUIDBeen().get(position);
        serviceUUID = characterBean.getServiceUUID();
        characterUUID = characterBean.getCharacterUUID();

        uuid_text.setText(characterUUID);


        String name = "";
        if (characterBean.isRead()) {
            name += "read";
            readBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isWrite()) {
            name += "write";
            writeBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isNotify()) {
            name += "notify";
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
