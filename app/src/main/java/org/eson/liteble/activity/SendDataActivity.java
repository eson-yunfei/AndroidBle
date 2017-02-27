package org.eson.liteble.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.eson.ble_sdk.util.BLEByteUtil;
import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.service.BleService;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/27
 * @说明：
 */

public class SendDataActivity extends BaseBleActivity {

	private EditText editText;
	private Button sendBtn;

	private String serviceUUID;
	private String characterUUID;

	@Override
	protected int getRootLayout() {
		return R.layout.activity_send_data;
	}

	@Override
	protected void initView() {
		super.initView();
		editText = findView(R.id.editText);
		sendBtn = findView(R.id.sendBtn);
	}

	@Override
	protected void initViewListener() {
		super.initViewListener();
		sendBtn.setOnClickListener(this);
	}

	@Override
	protected void process(Bundle savedInstanceState) {
		super.process(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		serviceUUID = bundle.getString("serviceUUID");
		characterUUID = bundle.getString("characterUUID");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		sendData();
	}

	//***************************************************************************************************//
	//***************************************************************************************************//

	private void sendData() {
		String data = editText.getText().toString();
		if (TextUtils.isEmpty(data)) {
			return;
		}

		if (data.length() % 2 != 0) {
			return;
		}


		byte[] buffer = BLEByteUtil.hexStringToByte(data);


		BleService.get().sendData(UUID.fromString(serviceUUID), UUID.fromString(characterUUID), buffer);
	}

	@Override
	protected void changerBleState(int state) {
		if (!MyApplication.getInstance().isForeground(SendDataActivity.class.getName())) {
			return;
		}
		super.changerBleState(state);

	}
}
