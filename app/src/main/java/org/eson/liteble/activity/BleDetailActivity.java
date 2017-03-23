package org.eson.liteble.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.e.ble.BLESdk;
import com.e.ble.control.BLEControl;
import com.e.ble.util.BLEConstant;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.adapter.DeviceDetailAdapter;
import org.eson.liteble.bean.ServiceBean;
import org.eson.liteble.bean.UUIDBean;
import org.eson.liteble.service.BleService;

import java.util.ArrayList;
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
	private ListView detailList;

	private DeviceDetailAdapter mDeviceDetailAdapter;

	private String mac = "";
	private boolean isConnect = true;

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
		detailList = (ListView) findViewById(R.id.detailList);
	}

	@Override
	protected void initViewListener() {
		super.initViewListener();


		disConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isConnect) {
					showProgress("断开设备。。。");
					BLEControl.get().disconnect(mac);

					isConnect = false;
					disProgress();
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
		if (!BLESdk.get().isPermitConnectMore()) {
			BLEControl.get().disconnect(mac);
		}
		this.finish();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	//***************************************************************************************************//
	//***************************************************************************************************//


	@Override
	protected void changeBleData(String uuid, String buffer, String deviceAddress) {

		if (!MyApplication.getInstance().isForeground(BleDetailActivity.class.getName())) {
			return;
		}
		super.changeBleData(uuid, buffer, deviceAddress);
	}

	@Override
	protected void changerBleState(int state) {
		super.changerBleState(state);
		disProgress();
		switch (state) {

			case BLEConstant.State.STATE_DIS_CONNECTED:
				isConnect = false;
				disConnect.setText("重新连接设备");
//				getMessage();
				break;
			case BLEConstant.Connection.STATE_CONNECT_SUCCEED:
			case BLEConstant.Connection.STATE_CONNECT_CONNECTED:
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
//		HashMap<String, String> serviceMap = gattServiceData.get(groupPosition);
//		HashMap<String, String> characterMap = gattCharacteristicData.get(groupPosition).get(childPosition);
//
//		String serviceUUID = serviceMap.get(LIST_UUID);
//		String characterUUID = characterMap.get(LIST_UUID);
//
//		Intent intent = new Intent(BleDetailActivity.this, CharacteristicActivity.class);
//		intent.putExtra("serviceUUID", serviceUUID);
//		intent.putExtra("characterUUID", characterUUID);
//		startActivity(intent);
	}

	//***************************************************************************************************//
	//***************************************************************************************************//

	/**
	 * 获取设备的服务和特性详情
	 */
	private void getMessage() {


		BluetoothGatt gatt = BLEControl.get().getBluetoothGatt(MyApplication.getInstance().getCurrentShowDevice());
		if (gatt == null) {
			if (textView == null) {
				return;
			}
			textView.setText("gatt == null");
			return;
		}


		List<BluetoothGattService> serviceArrayList = gatt.getServices();

		if (serviceArrayList == null || serviceArrayList.size() == 0) {
			return;
		}

		List<ServiceBean> serviceBeanList = new ArrayList<>();

		ServiceBean serviceBean;
		for (BluetoothGattService service : serviceArrayList) {
			serviceBean = new ServiceBean();
			UUID serviceUUID = service.getUuid();
			serviceBean.setServiceUUID(serviceUUID.toString());

			int serviceType = service.getType();
			String typeStr =
					(serviceType == BluetoothGattService.SERVICE_TYPE_PRIMARY)
							? "PRIMARY" : "SECONDARY";

			List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();

			if (gattCharacteristics == null || gattCharacteristics.size() == 0) {
				serviceBeanList.add(serviceBean);
				continue;
			}

			List<UUIDBean> uuidBeanList = new ArrayList<>();

			UUIDBean uuidBean;
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				uuidBean = new UUIDBean();
				UUID character = gattCharacteristic.getUuid();
				int properties = gattCharacteristic.getProperties();    //用于区分特性用途（读、写、通知）

				if ((properties & PROPERTY_READ) != 0) {
					uuidBean.setRead(true);
				}
				if ((properties & PROPERTY_WRITE) != 0) {
					uuidBean.setWrite(true);
				}
				if ((properties & PROPERTY_NOTIFY) != 0) {
					uuidBean.setNotice(true);
				}

				uuidBean.setUuid(character.toString());
				uuidBeanList.add(uuidBean);
			}
			serviceBean.setUUIDBeen(uuidBeanList);
			serviceBeanList.add(serviceBean);
		}


		if (serviceBeanList == null) {
			return;
		}
		mDeviceDetailAdapter = new DeviceDetailAdapter(mContext, serviceBeanList);
		detailList.setAdapter(mDeviceDetailAdapter);
	}

}
