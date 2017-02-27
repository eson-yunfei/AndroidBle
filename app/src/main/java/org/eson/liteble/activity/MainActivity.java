package org.eson.liteble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import org.eson.liteble.R;
import org.eson.liteble.RxBus;
import org.eson.liteble.adapter.ScanBLEAdapter;
import org.eson.liteble.service.BleService;
import org.eson.liteble.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private Button searchBtn;
	private ListView mListView;
	private Button checkBtn;
	private List<BLEDevice> deviceList = new ArrayList<>();
	private ScanBLEAdapter scanBLEAdapter;
	private ProgressDialog m_pDialog;

	private BLEDevice selectDevice = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initViewListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		subBleState();
	}

	private void initView() {
		//设置列表
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		searchBtn = (Button) findViewById(R.id.start_search);
		mListView = (ListView) findViewById(R.id.listview);
		checkBtn = (Button) findViewById(R.id.checkBle);


		scanBLEAdapter = new ScanBLEAdapter(this, deviceList);
		mListView.setAdapter(scanBLEAdapter);

	}


	private void initViewListener() {
		searchBtn.setOnClickListener(this);
		checkBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

				showProgress();

				selectDevice = deviceList.get(i);

				BLEScanner.get().stopScan();
				BleService.get().connectionDevice(MainActivity.this, selectDevice.getMac());

			}
		});
	}


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

	public void addScanBLE(final BLEDevice bleDevice) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!deviceList.contains(bleDevice)) {
					deviceList.add(bleDevice);
				}
				scanBLEAdapter.setDataList(deviceList);
				scanBLEAdapter.notifyDataSetChanged();
			}
		});
	}


	/**
	 * 扫描蓝牙设备
	 */
	private void searchDevice() {
		BLEScanner.get().startScan(0, null, null, new BLEScanListener() {
			@Override
			public void onScannerStart() {
				showProgress();
			}

			@Override
			public void onScanning(BLEDevice device) {
				hideProgress();
				addScanBLE(device);
			}


			@Override
			public void onScannerStop() {
				hideProgress();
			}

			@Override
			public void onScannerError() {
				hideProgress();
			}
		});
	}


	public void showProgress() {
		if (m_pDialog == null) {
			m_pDialog = new ProgressDialog(this);
			m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			m_pDialog.setMessage("请稍等。。。");
			m_pDialog.setIndeterminate(false);
			m_pDialog.setCancelable(true);
		}
		if (m_pDialog.isShowing()) {
			return;
		}

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

	private void startToSetting() {
		Intent intent = new Intent(MainActivity.this, SettingActivity.class);
		startActivity(intent);
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
				searchDevice();
				break;
		}
	}


	private void subBleState() {
		RxBus.getInstance().toObserverable().map(new Function<Object, Bundle>() {
			@Override
			public Bundle apply(Object o) throws Exception {
				return (Bundle) o;
			}
		}).subscribe(new Observer<Bundle>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(Bundle value) {

				boolean containsKey = value.containsKey(BLEConstant.Type.TYPE_STATE);
				LogUtil.e("subBleState onNext " + containsKey);

				if (!containsKey) {
					return;
				}

				int state = value.getInt(BLEConstant.Type.TYPE_STATE, 0);
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

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {

			}
		});
	}

	private void startToNext() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				hideProgress();
				Intent intent = new Intent(MainActivity.this, BleDetailActivity.class);
//
				intent.putExtra("macAddr", selectDevice.getMac());
				intent.putExtra("name", selectDevice.getName());
				startActivity(intent);
			}
		});

	}
}
