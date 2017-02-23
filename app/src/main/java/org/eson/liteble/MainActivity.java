package org.eson.liteble;

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
import org.eson.liteble.activity.SettingActivity;
import org.eson.liteble.adapter.ScanBLEAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

	private Button searchBtn;
	private ListView mListView;
	private Button checkBtn;
	private List<BLEDevice> deviceList = new ArrayList<>();
	private ScanBLEAdapter scanBLEAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		searchBtn = (Button) findViewById(R.id.start_search);
		mListView = (ListView) findViewById(R.id.listview);
		checkBtn = (Button) findViewById(R.id.checkBle);
		initView();
		initViewListener();
	}

	private void initView(){
		//设置列表
		scanBLEAdapter = new ScanBLEAdapter(this, deviceList);
		mListView.setAdapter(scanBLEAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//				if (loadingDialog.isShowing()) {
//					return;
//				}
//				loadingDialog.show();

//				BLEDevice device = deviceList.get(i);
				//TODO  连接蓝牙设备

			}
		});
	}

	private void initViewListener(){
		searchBtn.setOnClickListener(this);
		checkBtn.setOnClickListener(this);
	}

	private void checkBleEnable(){
		BLECheck.get().checkBleState(this, new BLECheckListener() {
			@Override
			public void noBluetoothPermission() {
				//没有蓝牙权限，申请
				BLECheck.get().requestBlePermission(MainActivity.this,"",0x01);
			}

			@Override
			public void notSupportBle() {

			}

			@Override
			public void bleClosing() {
				BLECheck.get().openBle();
			}

			@Override
			public void bleStateOK() {
				final Drawable yes = getResources().getDrawable(R.mipmap.icon_ok);
				checkBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,yes,null);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0x01){
			if(resultCode == RESULT_OK){
				LogUtil.e("开始扫描");
				searchDevice();
			}
		}
	}

	public void addScanBLE(final BLEDevice bleDevice) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(!deviceList.contains(bleDevice)){
					deviceList.add(bleDevice);
				}
				scanBLEAdapter.setDataList(deviceList);
				scanBLEAdapter.notifyDataSetChanged();
			}
		});
	}


	private void searchDevice(){
		BLEScanner.get().startScan(0, null, null, new BLEScanListener() {
			@Override
			public void onScannerStart() {

			}

			@Override
			public void onScanning(BLEDevice device) {
				addScanBLE(device);
			}


			@Override
			public void onScannerStop() {

			}

			@Override
			public void onScannerError() {

			}
		});
	}

	private void startToSetting(){
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
		switch (v.getId()){
			case R.id.checkBle:
				checkBleEnable();
				break;

			case R.id.start_search:
				LogUtil.e("开始扫描");
				searchDevice();
				break;
		}
	}
}
