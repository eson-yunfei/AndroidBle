package org.eson.liteble.activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.eson.ble_sdk.control.BLEControl;
import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/25
 * @说明： 蓝牙服务的特性详情页面
 */

public class CharacteristicActivity extends BaseBleActivity {

	private TextView uuid_text;
	private TextView properties_text;
	private Button btn;
	private ListView descListView;
	private ListView dataListView;

	private String serviceUUID;
	private String characterUUID;
	private List<String> descriptors = new ArrayList<>();
	private List<String> dataList = new ArrayList<>();

	private ArrayAdapter<String> dataListAdapter;
	private int btnType = 0;
	private boolean isListenerNotice = false;

	@Override
	protected int getRootLayout() {
		return R.layout.activity_characteristic;
	}

	@Override
	protected void initView() {
		super.initView();
		uuid_text = findView(R.id.uuid_text);
		properties_text = findView(R.id.properties_text);
		btn = findView(R.id.btn);
		descListView = findView(R.id.desc_listView);
		dataListView = findView(R.id.data_listView);
	}

	@Override
	protected void initViewListener() {
		super.initViewListener();
		btn.setOnClickListener(this);
	}

	@Override
	protected void process(Bundle savedInstanceState) {
		super.process(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		setData(bundle);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (btnType == 0) {

			Intent intent = new Intent(CharacteristicActivity.this, SendDataActivity.class);
			intent.putExtra("serviceUUID", serviceUUID);
			intent.putExtra("characterUUID", characterUUID);
			startActivity(intent);
		} else {

			enableNotice();
		}
	}

	//***************************************************************************************************//
	//***************************************************************************************************//

	@Override
	protected void changeBleData(String uuid, String buffer, String deviceAddress) {
		dataList.add(0, buffer);
		if (dataListAdapter == null) {
			dataListAdapter = new ArrayAdapter<>(CharacteristicActivity.this,
					android.R.layout.simple_list_item_1, android.R.id.text1, dataList);
			dataListView.setAdapter(dataListAdapter);
		} else {
			dataListAdapter.notifyDataSetChanged();
		}
	}


	//***************************************************************************************************//
	//***************************************************************************************************//


	/**
	 * 启动通知服务
	 */
	private void enableNotice() {

		isListenerNotice = !isListenerNotice;
		String text = isListenerNotice ? "取消监听" : "监听通知";
		btn.setText(text);
		BleService.get().enableNotify(MyApplication.getInstance().getCurrentShowDevice(),
				UUID.fromString(serviceUUID),
				UUID.fromString(characterUUID), UUID.fromString(descriptors.get(0)), isListenerNotice);
	}

	/**
	 * 发送数据
	 *
	 * @param bundle
	 */
	private void setData(Bundle bundle) {
		serviceUUID = bundle.getString("serviceUUID");
		characterUUID = bundle.getString("characterUUID");

		uuid_text.setText(characterUUID);

		BluetoothGatt bluetoothGatt = BLEControl.get().getBluetoothGatt(MyApplication.getInstance().getCurrentShowDevice());
		if (bluetoothGatt == null) {
			return;
		}
		BluetoothGattService service = bluetoothGatt
				.getService(UUID.fromString(serviceUUID));
		BluetoothGattCharacteristic characteristic = service
				.getCharacteristic(UUID.fromString(characterUUID));

		int properties = characteristic.getProperties();    //用于区分特性用途（读、写、通知）
		String name = "";
		if ((properties & PROPERTY_READ) != 0) {
			name += "读";
			btnType = 1;
		}
		if ((properties & PROPERTY_WRITE) != 0) {
			name += "写";
			btnType = 0;
		}
		if ((properties & PROPERTY_NOTIFY) != 0) {
			name += "通知";
			btnType = 1;
		}

		properties_text.setText(name);
		if (btnType == 0) {
			btn.setText("写命令");
		} else {
			btn.setText("监听通知");
		}


		List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();

		if (descriptorList.size() == 0) {
			return;
		}


		for (BluetoothGattDescriptor bluetoothGattDescriptor : descriptorList) {
			LogUtil.e("descriptor-->>" + bluetoothGattDescriptor.getUuid().toString());
			descriptors.add(bluetoothGattDescriptor.getUuid().toString());
		}

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CharacteristicActivity.this,
				android.R.layout.simple_list_item_1, android.R.id.text1, descriptors);
		descListView.setAdapter(arrayAdapter);
	}


}
