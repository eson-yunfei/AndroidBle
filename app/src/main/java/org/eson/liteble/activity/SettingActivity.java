package org.eson.liteble.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;

import com.e.ble.BLESdk;

import org.eson.liteble.activity.base.ViewBindActivity;
import org.eson.liteble.databinding.ActivitySettingBinding;
import org.eson.liteble.share.ConfigShare;
import org.eson.liteble.util.ToastUtil;

import static java.lang.Integer.parseInt;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 18:21
 * @change    新增过滤无名称设备
 * @chang 2020-06-19
 * @class describe
 */
public class SettingActivity extends ViewBindActivity {

    private ConfigShare configShare;
    private ActivitySettingBinding settingBinding;


    @Override
    protected View getBindViewRoot() {
        settingBinding = ActivitySettingBinding.inflate(getLayoutInflater());

        return settingBinding.getRoot();
    }

    @Override
    protected void onProcess() {
        configShare = new ConfigShare(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        settingBinding.timeOutEdit.setText("" + configShare.getConnectTime());

        boolean permit = configShare.isPermitConnectMore();
        int visibility = permit ? View.VISIBLE : View.GONE;

        settingBinding.maxSizeLayout.setVisibility(visibility);
        settingBinding.switchBtn.setChecked(permit);

        settingBinding.maxNumber.setText("" + configShare.getMaxConnect());
        settingBinding.switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {

            configShare.setPermitConnectMore(isChecked);
            int visibility1 = isChecked ? View.VISIBLE : View.GONE;
            settingBinding.maxSizeLayout.setVisibility(visibility1);
        });

        settingBinding.filterNoName.setChecked(configShare.getFilterNoName());
        settingBinding.filterNoName.setOnCheckedChangeListener((buttonView, isChecked) -> configShare.setFilterNoName(isChecked));

        settingBinding.saveBtn.setOnClickListener(v -> saveSettingInfo());

    }

    private void saveSettingInfo() {
        String text = settingBinding.timeOutEdit.getText().toString();

        if (TextUtils.isEmpty(text)) {
            ToastUtil.showShort(this, "超时时间不能为空");
            return;
        }
        int value = parseInt(text);
        if (value < -1) {
            value = -1;
        }
        configShare.setConnectTime(value);

        String maxConnect = settingBinding.switchBtn.isChecked() ? settingBinding.maxNumber.getText().toString() : "1";

        int size = Integer.parseInt(maxConnect);
        if (size < 1 || size > 5) {
            ToastUtil.showShort(this, "设备连接超过限制范围");
            return;
        }
        configShare.setMaxConnect(size);

        BLESdk.get().setMaxConnect(size);
    }
}
