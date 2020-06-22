package org.eson.liteble.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.e.ble.BLESdk;
import com.e.ble.control.BLEControl;

import org.eson.liteble.MyApplication;
import org.eson.liteble.R;
import org.eson.liteble.util.LogUtil;

public class DeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.e("DeviceActivity -->> onBackPressed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("DeviceActivity -->> onPause");
        if (!BLESdk.get().isPermitConnectMore()) {
            BLEControl.get().disconnect(MyApplication.getInstance().getCurrentShowDevice());
        }
    }
}