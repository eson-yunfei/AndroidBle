package org.eson.liteble.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.e.ble.check.BLECheck;
import com.e.ble.check.BLECheckListener;
import com.google.android.material.tabs.TabLayout;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.activity.fragment.BondedDevicesFragment;
import org.eson.liteble.activity.fragment.DeviceScanFragment;
import org.eson.liteble.util.LogUtil;
import org.eson.liteble.util.ToastUtil;

/**
 * 主界面，蓝牙状态检测，蓝牙搜索界面
 */
public class MainActivity extends BaseBleActivity {

    private ActionBar mActionBar;
    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private DeviceScanFragment mScanFragment;
    private BondedDevicesFragment mDevicesFragment;

    private MenuItem menuRefresh;
    private MenuItem menuScan;

    private int currentIndex = 0;

    @Override
    protected int getRootLayout() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {

        mActionBar = getSupportActionBar();
        if (Build.VERSION.SDK_INT >= 21) {
            mActionBar.setElevation(0);
        }

        mTabLayout = findView(R.id.tabLayout);

        mScanFragment = new DeviceScanFragment();
        mDevicesFragment = new BondedDevicesFragment();

        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.containerLayout, mScanFragment);
        mTransaction.commit();

        mTabLayout.addTab(mTabLayout.newTab().setText("Scanner"), true);
        mTabLayout.addTab(mTabLayout.newTab().setText("Bonded"));


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabChanged(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void onTabChanged(TabLayout.Tab tab) {
        currentIndex = tab.getPosition();
        mTransaction = mFragmentManager.beginTransaction();

        reSetMenu();

        switch (currentIndex) {
            case 0:
                mTransaction.replace(R.id.containerLayout, mScanFragment);
                break;
            case 1:
                mTransaction.replace(R.id.containerLayout, mDevicesFragment);
                menuScan.setVisible(false);
                break;

        }
        mTransaction.commit();


    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);

        checkBLEState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reSetMenu();
    }

    public void reSetMenu() {
        if (menuRefresh == null) {
            return;
        }
        menuRefresh.setVisible(false);
        menuRefresh.setActionView(null);
        menuScan.setTitle("Scan");
        menuScan.setVisible(true);
    }

    private void checkBLEState() {
        BLECheck.get().checkBleState(mContext, new BLECheckListener() {
            @Override
            public void noBluetoothPermission() {
                //没有蓝牙权限，申请
                BLECheck.get().requestBlePermission(MainActivity.this, "", 0x01);
            }

            @Override
            public void notSupportBle() {
                ToastUtil.showShort(mContext, "Sorry,UnSupport BLE !");
                finish();
            }

            @Override
            public void bleClosing() {
                BLECheck.get().openBle(MainActivity.this, 0x02);
            }

            @Override
            public void bleStateOK() {
                //DO NOTHING
            }
        });
    }

    @Override
    protected void changeBleData(String uuid, byte[] buffer, String deviceAddress) {
        if (!MyApplication.getInstance().isForeground(MainActivity.class.getName())) {
            return;
        }
        super.changeBleData(uuid, buffer, deviceAddress);
    }

    @Override
    protected void changerBleState(String mac, int state) {
        super.changerBleState(mac, state);

        if (currentIndex == 0) {
            mScanFragment.onBleStateChange(mac, state);
        } else {
            mDevicesFragment.onBleStateChange(mac, state);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuRefresh = menu.findItem(R.id.menu_refresh);
        menuScan = menu.findItem(R.id.menu_scan);

        reSetMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startToSetting();
            return true;
        }

        if (id == R.id.action_about) {
            startToAbout();
            return true;
        }
        if (id == R.id.menu_scan) {
            if (menuRefresh.isVisible()) {
                mScanFragment.stopScanner();
                reSetMenu();
            } else {
                mScanFragment.startScanner();
                menuRefresh.setVisible(true);
                menuRefresh.setActionView(R.layout.action_bar_progress);
                menuScan.setTitle("Scanning");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void startToAbout() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }


    private void startToSetting() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("onActivityResult" + requestCode + ";;;;" + resultCode);
        if (requestCode == 0x01) {
            if (resultCode != RESULT_OK) {

                return;
            }

            ToastUtil.showShort(mContext, "获取蓝牙权限成功");
            checkBLEState();
        } else if (requestCode == 0x02) {
            if (resultCode != RESULT_OK) {
                return;
            }
            ToastUtil.showShort(mContext, "打开蓝牙成功");
            checkBLEState();
        }
    }

}
