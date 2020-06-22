/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.eson.liteble.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLE_UUID_Util;

import org.eson.liteble.R;
import org.eson.liteble.ble.bean.BleDataBean;

import java.util.List;

/**
 * @package_name org.eson.liteble.activity.adapter
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/17.
 * @description
 */

public class BleDataAdapter extends MyBaseAdapter<BleDataBean> {


    private boolean isUnknownCharacter = false;

    public BleDataAdapter(Context context, List<BleDataBean> dataList, String characterName) {
        super(context, dataList);
        isUnknownCharacter = TextUtils.equals(characterName, BLE_UUID_Util.UNKNOWN_CHARACTER);
    }

    @Override
    public View getView(int position, View view, ViewGroup var3) {

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_data, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BleDataBean bleDataBean = dataList.get(position);
        String time = bleDataBean.getTime() + " : ";
        String dataString = getDataString(bleDataBean.getBuffer());


        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(time + "\n\n"+dataString);

        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK),
                0, time.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.mTextView.setText(spannableStringBuilder);
        return view;
    }

    class ViewHolder {

        private TextView mTextView;

        public ViewHolder(View rootView) {
            mTextView = findView(rootView, R.id.data_text);
        }
    }


    public String getDataString(byte[] buffer) {
        String text = BLEByteUtil.getHexString(buffer);
        if (!isUnknownCharacter) {
            text = text + " (  " + BLEByteUtil.byteToCharSequence(buffer) + "  )";
        }
        return text;
    }
}
