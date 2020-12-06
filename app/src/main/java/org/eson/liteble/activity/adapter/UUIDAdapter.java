package org.eson.liteble.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shon.bluetooth.util.BleUUIDUtil;

import org.eson.liteble.R;
import org.eson.liteble.activity.bean.CharacterBean;

import java.util.List;
import java.util.UUID;

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
        ViewHolder viewHolder;
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
        String characterName = BleUUIDUtil.getCharacterNameByUUID(UUID.fromString(characterUUID));
        String hexString = BleUUIDUtil.getHexValue(UUID.fromString(characterUUID));

        viewHolder.character_name_text.setText(characterName);
        viewHolder.uuidText.setText(resources.getString(R.string.service_uuid, hexString, characterUUID));
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
        public TextView character_name_text;
        public TextView uuidText;
        public TextView descText;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            initViews();
        }

        private void initViews() {
            character_name_text = findView(rootView, R.id.character_name_text);
            uuidText = findView(rootView, R.id.character_text);
            descText = findView(rootView, R.id.des_text);
        }
    }


}
