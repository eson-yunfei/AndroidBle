package org.eson.liteble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.eson.ble_sdk.bean.BLEDevice;
import org.eson.ble_sdk.check.BLECheck;
import org.eson.ble_sdk.check.BLECheckListener;
import org.eson.ble_sdk.scan.BLEScanListener;
import org.eson.ble_sdk.scan.BLEScanner;
import org.eson.ble_sdk.util.BLEConstant;
import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.adapter.ScanBLEAdapter;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.LogUtil;
import org.eson.liteble.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面，蓝牙状态检测，蓝牙搜索界面
 */
public class MainActivity extends BaseBleActivity {

	private Button searchBtn;
	private ListView mListView;
	private Button checkBtn;
	private List<BLEDevice> deviceList = new ArrayList<>();
	private ScanBLEAdapter scanBLEAdapter;
	private ProgressDialog m_pDialog;

	private BLEDevice selectDevice = null;

	@Override
	protected int getRootLayout() {
		return R.layout.activity_main;
	}


	@Override
	protected void initView() {
		//设置列表
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		searchBtn = findView(R.id.start_search);
		mListView = findView(R.id.listview);
		checkBtn = findView(R.id.checkBle);

		scanBLEAdapter = new ScanBLEAdapter(this, deviceList);
		mListView.setAdapter(scanBLEAdapter);

	}

	@Override
	protected void initViewListener() {
		searchBtn.setOnClickListener(this);
		checkBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

				selectDevice = deviceList.get(i);
				showProgress("正在连接设备：" + selectDevice.getName());

				BLEScanner.get().stopScan();
				BleService.get().connectionDevice(MainActivity.this, selectDevice.getMac());

			}
		});
	}

	///***********************************************************************************************//
	///***********************************************************************************************//


	@Override
	protected void changeBleData(String uuid, String buffer) {
		if (!MyApplication.getInstance().isForeground(MainActivity.class.getName())) {
			return;
		}
		super.changeBleData(uuid, buffer);
	}

	@Override
	protected void changerBleState(int state) {
		super.changerBleState(state);

		switch (state) {
			case BLEConstant.State.STATE_CONNECTED:

				break;
			case BLEConstant.State.STATE_CONNECTING:
				break;
			case BLEConstant.State.STATE_DIS_CONNECTED:
				break;
			case BLEConstant.State.STATE_DIS_CONNECTING:
				break;
			case BLEConstant.State.STATE_DISCOVER_SERVER:
				startToNext();
				break;
		}

	}

	///***********************************************************************************************//
	///***********************************************************************************************//
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.e("onActivityResult" + requestCode + ";;;;" + resultCode);
		if (requestCode == 0x01) {
			if (resultCode == RESULT_OK) {
			}
		} else if (requestCode == 0x02) {
			if (resultCode == RESULT_OK) {
				final Drawable yes = getResources().getDrawable(R.mipmap.icon_ok);
				checkBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, yes, null);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			LogUtil.e("setting ~~~~ ");
			startToSetting();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.checkBle:
				checkBleEnable();
				break;

			case R.id.start_search:
				LogUtil.e("开始扫描");
				deviceList.clear();
				scanBLEAdapter.notifyDataSetChanged();
				searchDevice();
				break;
		}
	}
	///***********************************************************************************************//
	///***********************************************************************************************//

	/**
	 * 显示等待框
	 *
	 * @param msg
	 */
	public void showProgress(String msg) {
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
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				m_pDialog.show();
			}
		});

	}

	public void hideProgress() {

		if (m_pDialog == null) {
			return;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				m_pDialog.dismiss();
			}
		});

	}

	///***********************************************************************************************//
	///***********************************************************************************************//
	private void checkBleEnable() {
		BLECheck.get().checkBleState(this, new BLECheckListener() {
			@Override
			public void noBluetoothPermission() {
				//没有蓝牙权限，申请
				BLECheck.get().requestBlePermission(MainActivity.this, "", 0x01);
			}

			@Override
			public void notSupportBle() {
			}

			@Override
			public void bleClosing() {
				BLECheck.get().openBle(MainActivity.this, 0x02);
			}

			@Override
			public void bleStateOK() {
				final Drawable yes = getResources().getDrawable(R.mipmap.icon_ok);
				checkBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, yes, null);
			}
		});
	}

	/**
	 * 扫描蓝牙设备
	 */
	private void searchDevice() {
		showProgress("搜索设备中。。。。");
		BLEScanner.get().startScan(0, null, null, new BLEScanListener() {
			@Override
			public void onScannerStart() {

			}

			@Override
			public void onScanning(BLEDevice device) {
				hideProgress();
				addScanBLE(device);
			}


			@Override
			public void onScannerStop() {
				hideProgress();
				ToastUtil.showShort(mContext, "扫描结束");
			}

			@Override
			public void onScannerError() {
				hideProgress();
				ToastUtil.showShort(mContext, "扫描出现异常");
			}
		});
	}

	public void addScanBLE(final BLEDevice bleDevice) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!deviceList.contains(bleDevice)) {
					deviceList.add(0, bleDevice);
				}
				scanBLEAdapter.notifyDataSetChanged();
			}
		});
	}

	///***********************************************************************************************//
	private void startToSetting() {
		Intent intent = new Intent(MainActivity.this, SettingActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转的想起界面
	 */
	private void startToNext() {
		if (!MyApplication.getInstance().isForeground(MainActivity.class.getName())) {
			return;
		}
		hideProgress();

		ToastUtil.showShort(mContext, "连接成功");
		Intent intent = new Intent(MainActivity.this, BleDetailActivity.class);
//
		intent.putExtra("macAddr", selectDevice.getMac());
		intent.putExtra("name", selectDevice.getName());
		startActivity(intent);

	}
}
