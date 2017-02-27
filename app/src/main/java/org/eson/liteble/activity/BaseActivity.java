package org.eson.liteble.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/27
 * @说明：
 */

abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

	protected Context mContext = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getRootLayout());
		mContext = this;

		initView();
		initViewListener();

		process(savedInstanceState);
	}

	protected void initView() {

	}

	protected void initViewListener() {
	}

	protected void process(Bundle savedInstanceState) {

	}

	protected abstract int getRootLayout();

	@Override
	public void onClick(View v) {

	}


	protected <T extends View> T findView(int viewId) {
		return (T) findViewById(viewId);
	}
}
