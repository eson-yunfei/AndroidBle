package org.eson.liteble.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eson.liteble.R;
import org.eson.liteble.bean.CharacterBean;

import java.util.List;

/**
 * @author xiaoyunfei
 * @date: 2017/3/23
 * @Description：
 */

public class UUIDAdapter extends MyBaseAdapter<CharacterBean> {
	public UUIDAdapter(Context context, List<CharacterBean> dataList) {
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

		CharacterBean characterBean = dataList.get(position);
		if (characterBean == null) {
			return view;
		}
		String characterUUID = characterBean.getCharacterUUID();
		viewHolder.uuidText.setText(characterUUID);
		viewHolder.descText.setText(getDesc(characterBean));
		return view;
	}

	private String getDesc(CharacterBean characterBean) {

		String desc = "";
		if (characterBean.isWrite()) {
			desc += "write  ";
		}
		if (characterBean.isRead()) {
			desc += "read  ";
		}
		if (characterBean.isNotify()) {
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
