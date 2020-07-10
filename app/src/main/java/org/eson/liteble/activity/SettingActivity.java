package org.eson.liteble.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.e.ble.BLESdk;

import org.eson.liteble.activity.base.IBaseActivity;
import org.eson.liteble.databinding.ActivitySettingBinding;
import org.eson.liteble.share.ConfigShare;
import org.eson.liteble.util.ToastUtil;

import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 18:21
 * @change 新增过滤无名称设备
 * @chang 2020-06-19
 * @class describe
 */
public class SettingActivity extends IBaseActivity<ActivitySettingBinding> {

    private ConfigShare configShare;

    @Override
    protected ActivitySettingBinding getViewBing() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) {
            return;
        }
        supportActionBar.setHomeButtonEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onProcess() {
        configShare = new ConfigShare(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        viewBinding.timeOutEdit.setText("" + configShare.getConnectTime());

        boolean permit = configShare.isPermitConnectMore();
        int visibility = permit ? View.VISIBLE : View.GONE;

        viewBinding.maxSizeLayout.setVisibility(visibility);
        viewBinding.switchBtn.setChecked(permit);

        viewBinding.maxNumber.setText("" + configShare.getMaxConnect());
        viewBinding.switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {

            configShare.setPermitConnectMore(isChecked);
            int visibility1 = isChecked ? View.VISIBLE : View.GONE;
            viewBinding.maxSizeLayout.setVisibility(visibility1);
        });

        viewBinding.filterNoName.setChecked(configShare.getFilterNoName());


        viewBinding.filterNoName.setOnCheckedChangeListener((buttonView, isChecked) -> configShare.setFilterNoName(isChecked));

        viewBinding.saveBtn.setOnClickListener(v -> saveSettingInfo());

    }

    private void saveSettingInfo() {
        String text = viewBinding.timeOutEdit.getText().toString();

        if (TextUtils.isEmpty(text)) {
            ToastUtil.showShort(this, "超时时间不能为空");
            return;
        }

        int value = parseInt(text);
        if (value < -1) {
            value = -1;
        }

        configShare.setConnectTime(value);

        String maxConnect = viewBinding.switchBtn.isChecked() ? viewBinding.maxNumber.getText().toString() : "1";

        int size = Integer.parseInt(maxConnect);
        if (size < 1 || size > 5) {
            ToastUtil.showShort(this, "设备连接超过限制范围");
            return;
        }
        configShare.setMaxConnect(size);

        BLESdk.get().setMaxConnect(size);
        ToastUtil.showShort(this, "保存成功");
        finish();
    }
}
