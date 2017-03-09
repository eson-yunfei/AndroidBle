package org.eson.liteble.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import org.eson.ble_sdk.BLESdk;
import org.eson.liteble.R;
import org.eson.liteble.share.ConfigShare;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 18:21
 * @change
 * @chang time
 * @class describe
 */
public class SettingActivity extends AppCompatActivity {

	private Context mContext;
	private EditText timeOutEdit;
	private Switch aSwitch;
	private ConfigShare configShare;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		mContext = this;
		configShare = new ConfigShare(mContext);
		initViews();

	}

	private void initViews() {

		timeOutEdit = (EditText) findViewById(R.id.timeOutEdit);
		aSwitch = (Switch) findViewById(R.id.switchBtn);


	}

	@Override
	protected void onResume() {
		super.onResume();
		timeOutEdit.setText("" + configShare.getConnectTime());
		aSwitch.setChecked(configShare.isPermitConnectMore());

		aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				configShare.setPermitConnectMore(isChecked);
				BLESdk.get().setPermitConnectMore(isChecked);
			}
		});

		timeOutEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		String text = timeOutEdit.getText().toString();

		if (TextUtils.isEmpty(text)) {
			return;
		}
		int value = Integer.parseInt(text);
		if (value < -1) {
			value = -1;
		}
		configShare.setConnectTime(value);
	}
}
