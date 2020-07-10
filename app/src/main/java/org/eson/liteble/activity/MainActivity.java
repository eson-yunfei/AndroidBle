package org.eson.liteble.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import org.eson.liteble.R;
import org.eson.liteble.activity.base.IBaseActivity;
import org.eson.liteble.activity.fragment.BondedFragment;
import org.eson.liteble.activity.fragment.ScanFragment;
import org.eson.liteble.databinding.ActivityMainBinding;
import org.eson.liteble.util.ToastUtil;
import org.eson.permissions.PermissionRequest;

/**
 * 主界面，蓝牙状态检测，蓝牙搜索界面
 */
public class MainActivity extends IBaseActivity<ActivityMainBinding> {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private ScanFragment mScanFragment;
    private BondedFragment mDevicesFragment;

    private MenuItem menuRefresh;
    private MenuItem menuScan;


    @Override
    protected ActivityMainBinding getViewBing() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onProcess() {
        ActionBar mActionBar = getSupportActionBar();
        if (Build.VERSION.SDK_INT >= 21 && mActionBar != null) {

            mActionBar.setElevation(0);
        }

        mScanFragment = new ScanFragment();
        mDevicesFragment = new BondedFragment();

        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.containerLayout, mScanFragment);
        mTransaction.commit();

        TabLayout.Tab scanTab = viewBinding.tabLayout.newTab().setText("Scanner");
        TabLayout.Tab bondTab = viewBinding.tabLayout.newTab().setText("Bonded");

        viewBinding.tabLayout.addTab(scanTab, true);
        viewBinding.tabLayout.addTab(bondTab);

        viewBinding.tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        checkBLEState();
    }

    private void checkBLEState() {
        PermissionRequest request = new PermissionRequest(this);
        request.requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.BLUETOOTH_ADMIN);
        request.setOnRequestCallBack(granted -> {

            if (granted){
                ToastUtil.showShort(MainActivity.this, "获取蓝牙权限成功");
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        reSetMenu();
    }

    /**
     *
     */
    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
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
    };

    /**
     * @param tab tab
     */
    private void onTabChanged(TabLayout.Tab tab) {
        int currentIndex = tab.getPosition();
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

    public void reSetMenu() {
        if (menuRefresh == null) {
            return;
        }
        menuRefresh.setVisible(false);
        menuRefresh.setActionView(null);
        menuScan.setTitle("Scan");
        menuScan.setVisible(true);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        LogUtil.e("onActivityResult" + requestCode + ";;;;" + resultCode);
//        if (requestCode == 0x01) {
//            if (resultCode != RESULT_OK) {
//
//                return;
//            }
//
//            ToastUtil.showShort(this, "获取蓝牙权限成功");
////            checkBLEState();
//        } else if (requestCode == 0x02) {
//            if (resultCode != RESULT_OK) {
//                return;
//            }
//            ToastUtil.showShort(this, "打开蓝牙成功");
////            checkBLEState();
//        }
//    }


}
