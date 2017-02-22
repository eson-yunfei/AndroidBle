package org.eson.liteble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.eson.liteble.activity.SettingActivity;
import org.eson.liteble.adapter.ScanBLEAdapter;
import org.eson.liteble.bean.BleItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private Button searchBtn;
	private ListView mListView;

	private List<BleItem> deviceList = new ArrayList<>();
	private ScanBLEAdapter scanBLEAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		searchBtn = (Button) findViewById(R.id.start_search);
		mListView = (ListView) findViewById(R.id.listview);
		initView();
	}

	private void initView(){

		for (int i = 0; i < 5; i++) {
			BleItem bleItem = new BleItem();
			bleItem.setAddress("test mac");
			bleItem.setName("name");
			bleItem.setRssi((int) (Math.random()*100));
			deviceList.add(bleItem);
		}
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

				BleItem device = deviceList.get(i);
				//TODO  连接蓝牙设备

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

}
