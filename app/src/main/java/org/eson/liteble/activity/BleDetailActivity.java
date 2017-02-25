package org.eson.liteble.activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.eson.ble_sdk.control.BLEControl;
import org.eson.liteble.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/23 15:34
 * @change
 * @chang time
 * @class describe
 */
public class BleDetailActivity extends AppCompatActivity {

	private TextView textView;
	private TextView name;
	private ExpandableListView expandList;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";

	private List<HashMap<String, String>> gattServiceData = new ArrayList<>();
	private List<List<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initViews();


		Intent intent = getIntent();
//        String mac = intent.getStringExtra("macAddr");
		String devName = intent.getStringExtra("name");
		name.setText(devName);
		getMessage();
	}

	private void initViews() {
		textView = (TextView) findViewById(R.id.text);
		name = (TextView) findViewById(R.id.name);

		expandList = (ExpandableListView) findViewById(R.id.expandList);
//		expandList.setAdapter();


		expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


				goToCharacteristicDetail(groupPosition, childPosition);
				return false;
			}
		});

	}

	private void goToCharacteristicDetail(int groupPosition, int childPosition) {
		HashMap<String, String> serviceMap = gattServiceData.get(groupPosition);
		HashMap<String, String> characterMap = gattCharacteristicData.get(groupPosition).get(childPosition);

		String serviceUUID = serviceMap.get(LIST_UUID);
		String characterUUID = characterMap.get(LIST_UUID);


		Intent intent = new Intent(BleDetailActivity.this, CharacteristicActivity.class);

		intent.putExtra("serviceUUID", serviceUUID);
		intent.putExtra("characterUUID", characterUUID);

		startActivity(intent);

//		ToastUtil.showShort(BleDetailActivity.this, "UUID:" + uuid);
	}

	private void getMessage() {

		BluetoothGatt gatt = BLEControl.get().getBluetoothGatt();
		if (gatt == null) {
			textView.setText("gatt == null");
			return;
		}


		List<BluetoothGattService> serviceArrayList = gatt.getServices();

		if (serviceArrayList == null || serviceArrayList.size() == 0) {
			return;
		}


		for (BluetoothGattService bluetoothGattService : serviceArrayList) {
			HashMap<String, String> currentServiceData = new HashMap<>();
			int serviceType = bluetoothGattService.getType();
			String name = "";
			if (serviceType == BluetoothGattService.SERVICE_TYPE_PRIMARY) {

				name = "主服务";
			} else {
				name = "辅助服务";
			}

			currentServiceData.put(LIST_NAME, name);

			UUID uuid = bluetoothGattService.getUuid();
			currentServiceData.put(LIST_UUID, uuid.toString());
			gattServiceData.add(currentServiceData);
		}

		if (gattServiceData.size() == 0) {
			return;
		}


		for (HashMap<String, String> map : gattServiceData) {

			List<HashMap<String, String>> gattCharacteristicList = new ArrayList<>();
			UUID serviceUuid = UUID.fromString(map.get(LIST_UUID));

			List<BluetoothGattCharacteristic> gattCharacteristics = gatt.getService(serviceUuid).getCharacteristics();


			if (gattCharacteristics.size() == 0) {
				continue;
			}

			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				HashMap<String, String> gattCharacteristicMap = new HashMap<>();
				UUID uuid = gattCharacteristic.getUuid();
				gattCharacteristicMap.put(LIST_UUID, uuid.toString());

				int properties = gattCharacteristic.getProperties();    //用于区分特性用途（读、写、通知）
				String name = "";
				if ((properties & PROPERTY_READ) != 0) {
					name = "读";
				}
				if ((properties & PROPERTY_WRITE) != 0) {
					name = "写";
				}
				if ((properties & PROPERTY_NOTIFY) != 0) {
					name = "通知";
				}
				gattCharacteristicMap.put(LIST_NAME, name);
				gattCharacteristicList.add(gattCharacteristicMap);
			}

			gattCharacteristicData.add(gattCharacteristicList);
		}


		SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
				this,
				gattServiceData,
				android.R.layout.simple_expandable_list_item_2,
				new String[]{LIST_NAME, LIST_UUID},
				new int[]{android.R.id.text1, android.R.id.text2},
				gattCharacteristicData,
				android.R.layout.simple_expandable_list_item_2,
				new String[]{LIST_NAME, LIST_UUID},
				new int[]{android.R.id.text1, android.R.id.text2}
		);

		expandList.setAdapter(gattServiceAdapter);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BLEControl.get().disConnect();
	}
}
