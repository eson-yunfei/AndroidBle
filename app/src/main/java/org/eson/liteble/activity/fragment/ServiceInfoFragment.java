package org.eson.liteble.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.e.ble.util.BLE_UUID_Util;
import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.message.NotifyState;
import com.e.tool.ble.bean.message.ReadMessage;
import com.e.tool.ble.imp.OnRead;
import com.e.tool.ble.imp.OnWriteDescriptor;

import org.eson.liteble.LittleBleViewModel;
import org.eson.liteble.R;
import org.eson.liteble.activity.adapter.BleDataAdapter;
import org.eson.liteble.activity.base.BaseObserveFragment;
import org.eson.liteble.activity.bean.BleDataBean;
import org.eson.liteble.activity.bean.CharacterBean;
import org.eson.liteble.activity.bean.DescriptorBean;
import org.eson.liteble.activity.bean.ServiceBean;
import org.eson.liteble.databinding.ActivityCharacteristicBinding;
import org.eson.liteble.util.LogUtil;
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
public class ServiceInfoFragment extends BaseObserveFragment<ActivityCharacteristicBinding> implements View.OnClickListener {



    private CharacterBean characterBean;
    private String serviceUUID;
    private String characterUUID;
    private String characterName = "";
    private String connectMac;
    private boolean isListenerNotice = false;

    private List<String> descriptors = new ArrayList<>();
    private List<BleDataBean> bleDataList = new ArrayList<>();
    private BleDataAdapter adapter;

    @Override
    protected ActivityCharacteristicBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return ActivityCharacteristicBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected void initListener() {
        viewBinding.readBtn.setOnClickListener(this);
        viewBinding.writeBtn.setOnClickListener(this);
        viewBinding.notifyBtn.setOnClickListener(this);
    }

    @Override
    public void setArguments(@Nullable Bundle bundle) {
        super.setArguments(bundle);

        if (bundle == null) {
            return;
        }
        ServiceBean serviceBean = bundle.getParcelable("serviceBean");

        int position = bundle.getInt("position");

        connectMac = bundle.getString("address");
        if (serviceBean == null) {
            return;
        }
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

    public void startListener() {

        LittleBleViewModel.getViewModel().observerDataNotify()
                .observe(this, notifyMessage -> {
                    if (notifyMessage == null) {
                        return;
                    }
                    changeBleData(notifyMessage.getCharacteristicUUID().toString(),
                            notifyMessage.getBytes(), notifyMessage.getAddress());
                });

    }

    @Override
    public void onDeviceStateChange(String deviceMac, int currentState) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeBtn:

                Bundle bundle = new Bundle();
                bundle.putString("serviceUUID", serviceUUID);
                bundle.putString("characterUUID", characterUUID);
                bundle.putString("address", connectMac);
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

        ReadMessage readMessage = new ReadMessage();
        readMessage.setAddress(connectMac);
        readMessage.setServiceUUID(UUID.fromString(serviceUUID));
        readMessage.setCharacteristicUUID(UUID.fromString(characterUUID));
        BleTool.getInstance().getController()
                .read(readMessage, new OnRead() {
                    @Override
                    public void onReadMessage(ReadMessage readMessage) {
                        changeBleData(readMessage.getCharacteristicUUID().toString()
                                , readMessage.getBytes(), readMessage.getAddress());
                    }

                    @Override
                    public void onReadError() {

                    }
                });
    }

    /**
     * 启动通知服务
     */
    private void enableNotice() {


        isListenerNotice = !isListenerNotice;
        String text = isListenerNotice ? "取消监听" : "开始监听";
        viewBinding.notifyBtn.setText(text);
        characterBean.setListening(isListenerNotice);
        UUID des = null;
        if (descriptors.size() == 0) {
            des = UUIDFormat.DESC;
        } else {
            des = UUID.fromString(descriptors.get(0));
        }

        NotifyState notifyState = new NotifyState();
        notifyState.setAddress(connectMac);
        notifyState.setServiceUUID(UUID.fromString(serviceUUID));
        notifyState.setCharacteristicUUID(UUID.fromString(characterUUID));
        notifyState.setDesUUID(des);
        notifyState.setEnable(isListenerNotice);

        BleTool.getInstance().getController()
                .updateNotify(notifyState, new OnWriteDescriptor() {

                    @Override
                    public void onWriteDescriptorError() {
                        LogUtil.e("NotifyState : onWriteDescriptorError ");
                    }

                    @Override
                    public void onWriteDescriptor(NotifyState result) {

                        LogUtil.e("NotifyState : " + result.isResult());
                        if (result.isResult()) {
                            startListener();
                        }
                    }
                });

    }

    protected void changeBleData(String uuid, byte[] buffer, String deviceAddress) {

        BleDataBean bean = new BleDataBean(deviceAddress, UUID.fromString(uuid), buffer);
        bean.setTime(getCurrentTime());
        bleDataList.add(0, bean);
        if (adapter == null) {
            adapter = new BleDataAdapter(getActivity(), bleDataList, characterName);
            viewBinding.dataListView.setAdapter(adapter);
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


        viewBinding.uuidText.setText(characterUUID);


        String name = "";
        if (characterBean.isRead()) {
            name += "read ";
            viewBinding.readBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isWrite()) {
            name += "write ";
            viewBinding.writeBtn.setVisibility(View.VISIBLE);
        }
        if (characterBean.isNotify()) {
            name += "notify ";
            viewBinding.notifyBtn.setVisibility(View.VISIBLE);
        }

        viewBinding.propertiesText.setText(name);


        String text = isListenerNotice ? "取消监听" : "开始监听";
        viewBinding.notifyBtn.setText(text);


        List<DescriptorBean> descriptorList = characterBean.getDescriptorBeen();
        if (descriptorList == null || descriptorList.size() == 0) {
            viewBinding.descriptorLayout.setVisibility(View.GONE);
            return;
        }

        for (DescriptorBean descriptorBean : descriptorList) {
            String uuid = descriptorBean.getUUID();
            descriptors.add(uuid);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, descriptors);
        viewBinding.descListView.setAdapter(arrayAdapter);
    }


}
