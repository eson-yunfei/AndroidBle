package org.eson.liteble.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.eson.ble_sdk.control.BLEControl;
import org.eson.ble_sdk.util.BLEConstant;
import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.service.BleService;

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
 * @class describe  蓝牙详细信息界面
 */
public class BleDetailActivity extends BaseBleActivity {

	private TextView textView;
	private TextView name;
	private Button disConnect;
	private ExpandableListView expandList;

	private String mac = "";
	private boolean isConnect = true;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";

	private List<HashMap<String, String>> gattServiceData = new ArrayList<>();
	private List<List<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();

	private SimpleExpandableListAdapter gattServiceAdapter = null;

	private ProgressDialog m_pDialog;

	@Override
	protected int getRootLayout() {
		return R.layout.activity_detail;
	}

	@Override
	protected void initView() {
		super.initView();
		textView = (TextView) findViewById(R.id.text);
		name = (TextView) findViewById(R.id.name);
		disConnect = (Button) findViewById(R.id.disconnect);
		expandList = (ExpandableListView) findViewById(R.id.expandList);
	}

	@Override
	protected void initViewListener() {
		super.initViewListener();

		expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				goToCharacteristicDetail(groupPosition, childPosition);
				return false;
			}
		});

		disConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isConnect) {
					showProgress("断开设备。。。");
					BLEControl.get().disConnect();
					gattServiceData.clear();
					gattCharacteristicData.clear();
					gattServiceAdapter.notifyDataSetChanged();

					isConnect = false;
				} else {
					showProgress("重新连接设备。。。");
					BleService.get().connectionDevice(BleDetailActivity.this, mac);
				}
			}
		});
	}

	@Override
	protected void process(Bundle savedInstanceState) {
		super.process(savedInstanceState);
		Intent intent = getIntent();
		mac = intent.getStringExtra("macAddr");
		String devName = intent.getStringExtra("name");
		name.setText(devName);
		getMessage();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		BLEControl.get().disConnect();
		this.finish();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	//***************************************************************************************************//
	//***************************************************************************************************//


	@Override
	protected void changeBleData(String uuid, String buffer) {

		if (!MyApplication.getInstance().isForeground(BleDetailActivity.class.getName())) {
			return;
		}
		super.changeBleData(uuid, buffer);
	}

	@Override
	protected void changerBleState(int state) {
		super.changerBleState(state);
		disProgress();
		switch (state) {

			case BLEConstant.State.STATE_DIS_CONNECTED:
				isConnect = false;
				disConnect.setText("重新连接设备");

				break;
			case BLEConstant.State.STATE_DISCOVER_SERVER:
				isConnect = true;
				disConnect.setText("断开连接");
				getMessage();
				break;
		}

	}


	//***************************************************************************************************//
	//***************************************************************************************************//

	/**
	 * 显示等待框
	 *
	 * @param msg
	 */
	private void showProgress(String msg) {
		if (m_pDialog == null) {
			m_pDialog = new ProgressDialog(this);
			m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			m_pDialog.setIndeterminate(false);
			m_pDialog.setCancelable(true);
		}
		if (m_pDialog.isShowing()) {
			return;
		}

		m_pDialog.setMessage(msg);
		m_pDialog.show();

	}

	private void disProgress() {
		if (m_pDialog == null) {
			return;
		}
		m_pDialog.dismiss();
	}

	//***************************************************************************************************//
	//***************************************************************************************************//

	/**
	 * 跳转的特性的详情界面
	 *
	 * @param groupPosition
	 * @param childPosition
	 */
	private void goToCharacteristicDetail(int groupPosition, int childPosition) {
		HashMap<String, String> serviceMap = gattServiceData.get(groupPosition);
		HashMap<String, String> characterMap = gattCharacteristicData.get(groupPosition).get(childPosition);

		String serviceUUID = serviceMap.get(LIST_UUID);
		String characterUUID = characterMap.get(LIST_UUID);

		Intent intent = new Intent(BleDetailActivity.this, CharacteristicActivity.class);
		intent.putExtra("serviceUUID", serviceUUID);
		intent.putExtra("characterUUID", characterUUID);
		startActivity(intent);
	}

	//***************************************************************************************************//
	//***************************************************************************************************//

	/**
	 * 获取设备的服务和特性详情
	 */
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

				name = "PRIMARY";
			} else {
				name = "SECONDARY";
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


		if (gattServiceAdapter == null) {
			gattServiceAdapter = new SimpleExpandableListAdapter(
					this,
					gattServiceData, R.layout.item_two_line,
					new String[]{LIST_UUID, LIST_NAME},
					new int[]{R.id.text1, R.id.text2},
					gattCharacteristicData, R.layout.item_two_line,
					new String[]{LIST_UUID, LIST_NAME,},
					new int[]{R.id.text1, R.id.text2}
			);
			expandList.setAdapter(gattServiceAdapter);
		}
		gattServiceAdapter.notifyDataSetChanged();
	}

}
