package org.eson.liteble.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.eson.ble_sdk.control.BLEControl;
import org.eson.ble_sdk.util.BLEConstant;
import org.eson.liteble.R;
import org.eson.liteble.RxBus;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

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
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		initViews();

		Intent intent = getIntent();
		mac = intent.getStringExtra("macAddr");
		String devName = intent.getStringExtra("name");
		name.setText(devName);
		getMessage();

		initSateListener();
	}

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
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (m_pDialog == null) {
					return;
				}
				m_pDialog.dismiss();
			}
		});
	}

	private CompositeDisposable compositeDisposable;

	private void initSateListener() {

		if (compositeDisposable == null) {
			compositeDisposable = new CompositeDisposable();
			RxBus.getInstance().toObserverable()
					.map(new Function<Object, Bundle>() {
						@Override
						public Bundle apply(Object o) throws Exception {
							return (Bundle) o;
						}
					}).subscribe(new Observer<Bundle>() {
				@Override
				public void onSubscribe(Disposable d) {
					compositeDisposable.add(d);
				}

				@Override
				public void onNext(Bundle value) {

					boolean containsKey = value.containsKey(BLEConstant.Type.TYPE_STATE);
					LogUtil.e("subBleState onNext " + containsKey);

					if (!containsKey) {
						return;
					}

					disProgress();
					int state = value.getInt(BLEConstant.Type.TYPE_STATE, 0);
					switch (state) {

						case BLEConstant.State.STATE_DIS_CONNECTED:
							isConnect = false;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									disConnect.setText("重新连接设备");
								}
							});
							break;
						case BLEConstant.State.STATE_DISCOVER_SERVER:
							isConnect = true;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									disConnect.setText("断开连接");
									getMessage();
								}
							});
							break;
					}
				}

				@Override
				public void onError(Throwable e) {

				}

				@Override
				public void onComplete() {

				}
			});
		}
	}


	private void initViews() {
		textView = (TextView) findViewById(R.id.text);
		name = (TextView) findViewById(R.id.name);
		disConnect = (Button) findViewById(R.id.disconnect);

		expandList = (ExpandableListView) findViewById(R.id.expandList);

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
					gattServiceData,
					android.R.layout.simple_expandable_list_item_2,
					new String[]{LIST_UUID, LIST_NAME},
					new int[]{android.R.id.text1, android.R.id.text2},
					gattCharacteristicData,
					android.R.layout.simple_expandable_list_item_2,
					new String[]{LIST_UUID, LIST_NAME,},
					new int[]{android.R.id.text1, android.R.id.text2}
			);
			expandList.setAdapter(gattServiceAdapter);
		}
		gattServiceAdapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		BLEControl.get().disConnect();
		this.finish();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
			compositeDisposable.clear();
		}
		BLEControl.get().disConnect();
	}
}
