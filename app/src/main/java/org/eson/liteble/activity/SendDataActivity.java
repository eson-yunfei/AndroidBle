package org.eson.liteble.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.eson.ble_sdk.util.BLEByteUtil;
import org.eson.liteble.R;
import org.eson.liteble.service.BleService;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/27
 * @说明：
 */

public class SendDataActivity extends AppCompatActivity {

	private EditText editText;
	private Button sendBtn;

	private String serviceUUID;
	private String characterUUID;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_send_data);


		initViews();
		Bundle bundle = getIntent().getExtras();
		serviceUUID = bundle.getString("serviceUUID");
		characterUUID = bundle.getString("characterUUID");
	}

	private void initViews() {

		editText = (EditText) findViewById(R.id.editText);
		sendBtn = (Button) findViewById(R.id.sendBtn);

		sendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendData();
			}
		});
	}

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
}
