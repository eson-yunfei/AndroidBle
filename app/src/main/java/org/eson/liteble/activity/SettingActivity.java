package org.eson.liteble.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.shon.mvvm.base.ui.BaseBindingActivity;

import org.eson.liteble.databinding.ActivitySettingBinding;
import org.eson.liteble.share.ConfigShare;
import org.eson.toast.ToastUtils;

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
public class SettingActivity extends BaseBindingActivity<ActivitySettingBinding> {

    private ConfigShare configShare;

    @Override
    public void initViewState() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) {
            return;
        }
        supportActionBar.setHomeButtonEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onProcess(Bundle bundle) {
        configShare = new ConfigShare(this);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        binding.timeOutEdit.setText("" + configShare.getConnectTime());

        boolean permit = configShare.isPermitConnectMore();
        int visibility = permit ? View.VISIBLE : View.GONE;

        binding.maxSizeLayout.setVisibility(visibility);
        binding.switchBtn.setChecked(permit);

        binding.maxNumber.setText("" + configShare.getMaxConnect());
        binding.switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {

            configShare.setPermitConnectMore(isChecked);
            int visibility1 = isChecked ? View.VISIBLE : View.GONE;
            binding.maxSizeLayout.setVisibility(visibility1);
        });

        binding.filterNoName.setChecked(configShare.getFilterNoName());


        binding.filterNoName.setOnCheckedChangeListener((buttonView, isChecked) -> configShare.setFilterNoName(isChecked));

        binding.saveBtn.setOnClickListener(v -> saveSettingInfo());

    }

    private void saveSettingInfo() {
        String text = binding.timeOutEdit.getText().toString();

        if (TextUtils.isEmpty(text)) {
            ToastUtils.showShort(this, "超时时间不能为空");
            return;
        }

        int value = parseInt(text);
        if (value < -1) {
            value = -1;
        }

        configShare.setConnectTime(value);

        String maxConnect = binding.switchBtn.isChecked() ? binding.maxNumber.getText().toString() : "1";

        int size = Integer.parseInt(maxConnect);
        if (size < 1 || size > 5) {
            ToastUtils.showShort(this, "设备连接超过限制范围");
            return;
        }
        configShare.setMaxConnect(size);

//        BLESdk.get().setMaxConnect(size);
        ToastUtils.showShort(this, "保存成功");
        finish();
    }


}
