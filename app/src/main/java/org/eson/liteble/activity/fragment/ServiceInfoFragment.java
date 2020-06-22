package org.eson.liteble.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.e.ble.util.BLE_UUID_Util;
import com.shon.dispatcher.Dispatcher;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.adapter.BleDataAdapter;
import org.eson.liteble.bean.BleDataBean;
import org.eson.liteble.bean.CharacterBean;
import org.eson.liteble.bean.DescriptorBean;
import org.eson.liteble.bean.ServiceBean;
import org.eson.liteble.command.Command;
import org.eson.liteble.common.ConnectedDevice;
import org.eson.liteble.databinding.ActivityCharacteristicBinding;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.UUIDFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:15
 * Package name : org.eson.liteble.activity.fragment
 * Des :
 */
public class ServiceInfoFragment extends BaseObserveFragment implements View.OnClickListener {


    private ActivityCharacteristicBinding characteristicBinding;

    private CharacterBean characterBean;
    private String serviceUUID;
    private String characterUUID;
    private String characterName = "";
    private boolean isListenerNotice = false;

    private List<String> descriptors = new ArrayList<>();
    private List<BleDataBean> bleDataList = new ArrayList<>();
    private BleDataAdapter adapter;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        characteristicBinding = ActivityCharacteristicBinding.inflate(getLayoutInflater(), container, false);
        return characteristicBinding.getRoot();
    }

    @Override
    protected void initListener() {
        characteristicBinding.readBtn.setOnClickListener(this);
        characteristicBinding.writeBtn.setOnClickListener(this);
        characteristicBinding.notifyBtn.setOnClickListener(this);
    }

    @Override
    public void setArguments(@Nullable Bundle bundle) {
        super.setArguments(bundle);

        if (bundle == null) {
            return;
        }
        int parentPosition = bundle.getInt("parentPosition");
        int position = bundle.getInt("position");

        String connectMac = MyApplication.getInstance().getCurrentShowDevice();
        ServiceBean serviceBean = ConnectedDevice.get().getServiceList(connectMac).get(parentPosition);

        characterBean = serviceBean.getUUIDBeen().get(position);
        serviceUUID = characterBean.getServiceUUID();
        characterUUID = characterBean.getCharacterUUID();
        characterName = BLE_UUID_Util.getCharacterNameByUUID(UUID.fromString(characterUUID));

        isListenerNotice = characterBean.isListening();
    }

    @Override
    public void onResume() {
        super.onResume();

        setData();
    }

    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {

        Command command = Dispatcher.getInstance().getApi();
//        command.startListener().execute((s, message) -> {
//            BleDataBean bleDataBean = (BleDataBean) message.getObject();
//            changeBleData(bleDataBean.getUuid().toString(), message.getBytes(), s);
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeBtn:

                Bundle bundle = new Bundle();
                bundle.putString("serviceUUID", serviceUUID);
                bundle.putString("characterUUID", characterUUID);
                navigateNext(R.id.action_serviceInfoFragment_to_sendDataFragment, bundle);
                break;
            case R.id.readBtn:
                readCharacter();

                break;
            case R.id.notifyBtn:
                enableNotice();
                break;
            default:
                break;

        }
    }

    private void readCharacter() {
        BleService.get().readCharacter(UUID.fromString(serviceUUID),
                UUID.fromString(characterUUID));
    }

    /**
     * 启动通知服务
     */
    private void enableNotice() {

        isListenerNotice = !isListenerNotice;
        String text = isListenerNotice ? "取消监听" : "开始监听";
        characteristicBinding.notifyBtn.setText(text);
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

    protected void changeBleData(String uuid, byte[] buffer, String deviceAddress) {

        BleDataBean bean = new BleDataBean(deviceAddress, UUID.fromString(uuid), buffer);
        bean.setTime(getCurrentTime());
        bleDataList.add(0, bean);
        if (adapter == null) {
            adapter = new BleDataAdapter(getActivity(), bleDataList, characterName);
            characteristicBinding.dataListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss ,  SSS", Locale.getDefault());

    private String getCurrentTime() {
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     *
     */
    private void setData() {


        characteristicBinding.uuidText.setText(characterUUID);


        String name = "";
        if (characterBean.isRead()) {
            name += "read ";
            characteristicBinding.readBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isWrite()) {
            name += "write ";
            characteristicBinding.writeBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isNotify()) {
            name += "notify ";
            characteristicBinding.notifyBtn.setVisibility(View.VISIBLE);
        }

        characteristicBinding.propertiesText.setText(name);


        String text = isListenerNotice ? "取消监听" : "开始监听";
        characteristicBinding.notifyBtn.setText(text);


        List<DescriptorBean> descriptorList = characterBean.getDescriptorBeen();
        if (descriptorList == null || descriptorList.size() == 0) {
            characteristicBinding.descriptorLayout.setVisibility(View.GONE);
            return;
        }

        for (DescriptorBean descriptorBean : descriptorList) {
            String uuid = descriptorBean.getUUID();
            descriptors.add(uuid);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, descriptors);
        characteristicBinding.descListView.setAdapter(arrayAdapter);
    }


}
