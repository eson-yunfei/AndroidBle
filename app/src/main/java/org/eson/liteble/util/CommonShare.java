package org.eson.liteble.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/3
 * @说明：
 */

public abstract class CommonShare {

	protected SharedPreferences sharePre;

	public CommonShare(Context context, String name) {
		if (context == null || name == null) {
			throw new RuntimeException("CommonPreferences params error !!!");
		}
		this.sharePre = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	protected float getValue(String key, float defValue) {
		return this.sharePre.getFloat(key, defValue);
	}

	protected void setValue(String key, float defValue) {
		this.sharePre.edit().putFloat(key, defValue).commit();
	}

	protected int getValue(String key, int defValue) {
		return this.sharePre.getInt(key, defValue);
	}

	protected void setValue(String key, int defValue) {
		this.sharePre.edit().putInt(key, defValue).commit();
	}

	protected long getValue(String key, long defValue) {
		return this.sharePre.getLong(key, defValue);
	}

	protected void setValue(String key, long defValue) {
		this.sharePre.edit().putLong(key, defValue).commit();
	}

	protected String getValue(String key, String defValue) {
		return this.sharePre.getString(key, defValue);
	}

	protected void setValue(String key, String defValue) {
		this.sharePre.edit().putString(key, defValue).commit();
	}

	protected Set<String> getValue(String key, Set<String> defValue) {
		return this.sharePre.getStringSet(key, defValue);
	}

	protected void setValue(String key, Set<String> defValue) {
		this.sharePre.edit().putStringSet(key, defValue).commit();
	}

	protected boolean getValue(String key, boolean defValue) {
		return this.sharePre.getBoolean(key, defValue);
	}

	protected void setValue(String key, boolean defValue) {
		this.sharePre.edit().putBoolean(key, defValue).commit();
	}

	public boolean remove(String key) {
		return sharePre.edit().remove(key).commit();
	}

	protected void clear() {
		this.sharePre.edit().clear().commit();
	}

}
