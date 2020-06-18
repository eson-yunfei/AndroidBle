package org.eson.liteble.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.e.ble.BLESdk;

import org.eson.liteble.R;
import org.eson.liteble.share.ConfigShare;
import org.eson.liteble.util.ToastUtil;

import static java.lang.Integer.parseInt;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 18:21
 * @change
 * @chang time
 * @class describe
 */
public class SettingActivity extends AppCompatActivity {

    private Context mContext;
    private EditText timeOutEdit;
    private Switch aSwitch;
    private ConfigShare configShare;
    private RelativeLayout maxSizeLayout;
    private EditText maxConnectSize;

    private Button saveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.eson.liteble.R.layout.activity_setting);

        mContext = this;
        configShare = new ConfigShare(mContext);
        initViews();

    }

    private void initViews() {

        timeOutEdit = (EditText) findViewById(org.eson.liteble.R.id.timeOutEdit);
        maxSizeLayout = (RelativeLayout) findViewById(R.id.maxSizeLayout);
        maxConnectSize = (EditText) findViewById(R.id.maxNumber);
        aSwitch = (Switch) findViewById(org.eson.liteble.R.id.switchBtn);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        maxSizeLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        timeOutEdit.setText("" + configShare.getConnectTime());

        boolean permit = configShare.isPermitConnectMore();
        int visibility = permit ? View.VISIBLE : View.GONE;

        maxSizeLayout.setVisibility(visibility);
        aSwitch.setChecked(permit);

        maxConnectSize.setText("" + configShare.getMaxConnect());
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                configShare.setPermitConnectMore(isChecked);
                int visibility = isChecked ? View.VISIBLE : View.GONE;
                maxSizeLayout.setVisibility(visibility);


            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettingInfo();
            }
        });

    }

    private void saveSettingInfo() {
        String text = timeOutEdit.getText().toString();

        if (TextUtils.isEmpty(text)) {
            ToastUtil.showShort(mContext, "超时时间不能为空");
            return;
        }
        int value = parseInt(text);
        if (value < -1) {
            value = -1;
        }
        configShare.setConnectTime(value);

        String maxConnect = aSwitch.isChecked() ? maxConnectSize.getText().toString() : "1";

        int size = Integer.parseInt(maxConnect);
        if (size < 1 || size > 5) {
            ToastUtil.showShort(mContext, "设备连接超过限制范围");
            return;
        }
        configShare.setMaxConnect(size);

        BLESdk.get().setMaxConnect(size);
    }
}
