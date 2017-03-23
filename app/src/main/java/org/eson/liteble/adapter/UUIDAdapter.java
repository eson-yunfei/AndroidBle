package org.eson.liteble.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eson.liteble.R;
import org.eson.liteble.bean.UUIDBean;

import java.util.List;

/**
 * @author xiaoyunfei
 * @date: 2017/3/23
 * @Description：
 */

public class UUIDAdapter extends MyBaseAdapter<UUIDBean> {
	public UUIDAdapter(Context context, List<UUIDBean> dataList) {
		super(context, dataList);
	}

	@Override
	public View getView(int position, View view, ViewGroup var3) {
		ViewHolder viewHolder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.item_uuid, null);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		UUIDBean uuidBean = dataList.get(position);
		if (uuidBean == null) {
			return view;
		}
		viewHolder.uuidText.setText(uuidBean.getUuid());
		viewHolder.descText.setText(getDesc(uuidBean));
		return view;
	}

	private String getDesc(UUIDBean uuidBean) {

		String desc = "";
		if (uuidBean.isWrite()) {
			desc += "write  ";
		}
		if (uuidBean.isRead()) {
			desc += "read  ";
		}
		if (uuidBean.isNotice()) {
			desc += "notify  ";
		}
		return desc;
	}


	private class ViewHolder {
		private View rootView;
		public TextView uuidText;
		public TextView descText;

		ViewHolder(View rootView) {
			this.rootView = rootView;
			initViews();
		}

		private void initViews() {
			uuidText = findView(rootView, R.id.character_text);
			descText = findView(rootView, R.id.des_text);
		}
	}


}
