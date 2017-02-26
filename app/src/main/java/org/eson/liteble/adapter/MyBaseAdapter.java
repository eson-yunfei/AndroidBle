package org.eson.liteble.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.adapter
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 15:38
 * @change
 * @chang time
 * @class describe
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	protected Context context;
	protected List<T> dataList;
	protected LayoutInflater inflater;
	protected Resources resources;

	public MyBaseAdapter(Context context, List<T> dataList) {
		this.context = context;
		this.dataList = dataList;
		this.inflater = LayoutInflater.from(context);
		this.resources = context.getResources();
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public int getCount() {
		return this.dataList == null ? 0 : this.dataList.size();
	}

	public T getItem(int i) {
		return this.dataList == null ? null : this.dataList.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public abstract View getView(int var1, View var2, ViewGroup var3);

	protected <T extends View> T findView(View rootView, int viewID) {
		return (T) rootView.findViewById(viewID);
	}
}