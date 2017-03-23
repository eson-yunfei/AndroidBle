package org.eson.liteble.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.eson.liteble.R;
import org.eson.liteble.activity.CharacteristicActivity;
import org.eson.liteble.bean.ServiceBean;
import org.eson.liteble.bean.UUIDBean;

import java.util.List;

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

		String serviceUUIDString = context.getString(R.string.service_uuid, serviceBean.getServiceUUID());

		viewHolder.mTextView.setText(serviceUUIDString);
		List<UUIDBean> uuidBeanList = serviceBean.getUUIDBeen();

		if (uuidBeanList != null) {
			UUIDAdapter uuidAdapter = new UUIDAdapter(context, uuidBeanList);
			viewHolder.mListView.setAdapter(uuidAdapter);
			setItemClickListener(viewHolder.mListView, serviceBean.getServiceUUID(), uuidBeanList);
		}
		return view;
	}

	private void setItemClickListener(ListView listView, final String serviceUUID, final List<UUIDBean> uuidBeanList) {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UUIDBean uuidBean = uuidBeanList.get(position);

				Intent intent = new Intent(context, CharacteristicActivity.class);
				intent.putExtra("serviceUUID", serviceUUID);
				intent.putExtra("characterUUID", uuidBean.getUuid());

				context.startActivity(intent);
			}
		});
	}

	private class ViewHolder {
		private View rootView;
		public TextView mTextView;
		public ListView mListView;

		ViewHolder(View rootView) {
			this.rootView = rootView;
			initViews();
		}

		private void initViews() {
			mTextView = findView(rootView, R.id.server_uuid_text);
			mListView = findView(rootView, R.id.character_list);
		}
	}
}
