package org.eson.liteble.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.e.ble.util.BLE_UUID_Util;

import org.eson.liteble.R;
import org.eson.liteble.activity.CharacteristicActivity;
import org.eson.liteble.bean.ServiceBean;
import org.eson.liteble.bean.CharacterBean;

import java.util.List;
import java.util.UUID;

/**
 * @author xiaoyunfei
 * @date: 2017/3/22
 * @Description：
 */

public class DeviceDetailAdapter extends MyBaseAdapter<ServiceBean> {

    public DeviceDetailAdapter(Context context, List<ServiceBean> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup var3) {

        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_uuid_group, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ServiceBean serviceBean = dataList.get(position);
        String uuid = serviceBean.getServiceUUID();
        if (TextUtils.isEmpty(uuid)) {
            return view;
        }
        String serviceName = BLE_UUID_Util.getServiceNameByUUID(UUID.fromString(uuid));
        String shortUUID = BLE_UUID_Util.getHexValue(UUID.fromString(uuid));
        String serviceUUIDString = context.getString(R.string.service_uuid, shortUUID, serviceBean.getServiceUUID());
        String serviceType = serviceBean.getServiceType();

        viewHolder.serviceNameText.setText(serviceName);
        viewHolder.mTextView.setText(serviceUUIDString);
        viewHolder.serviceTypeText.setText(serviceType + " Service");

        List<CharacterBean> characterBeanList = serviceBean.getUUIDBeen();

        if (characterBeanList != null) {
            UUIDAdapter uuidAdapter = new UUIDAdapter(context, characterBeanList);
            viewHolder.mListView.setAdapter(uuidAdapter);
            setItemClickListener(viewHolder.mListView, position, serviceBean.getServiceUUID(), characterBeanList);
        }
        return view;
    }

    private void setItemClickListener(ListView listView, final int parentPosition,
                                      final String serviceUUID,
                                      final List<CharacterBean> characterBeanList) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CharacterBean characterBean = characterBeanList.get(position);

                Intent intent = new Intent(context, CharacteristicActivity.class);
//                intent.putExtra("character", characterBean);
                intent.putExtra("parentPosition", parentPosition);
                intent.putExtra("position", position);

                context.startActivity(intent);
            }
        });
    }

    private class ViewHolder {
        private View rootView;
        public TextView serviceNameText;
        public TextView mTextView;
        public TextView serviceTypeText;

        public ListView mListView;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            initViews();
        }

        private void initViews() {
            serviceNameText = findView(rootView, R.id.server_name_text);
            mTextView = findView(rootView, R.id.server_uuid_text);
            mListView = findView(rootView, R.id.character_list);
            serviceTypeText = findView(rootView, R.id.server_type_text);
        }
    }
}
