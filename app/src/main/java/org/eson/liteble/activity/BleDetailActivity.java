package org.eson.liteble.activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.eson.ble_sdk.control.BLEControl;
import org.eson.liteble.util.LogUtil;
import org.eson.liteble.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/23 15:34
 * @change
 * @chang time
 * @class describe
 */
public class BleDetailActivity extends AppCompatActivity {

    private TextView textView;
    private TextView name;
    private TextView service1, service2, service3, service4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textView = (TextView) findViewById(R.id.text);
        name = (TextView) findViewById(R.id.name);
        service1 = (TextView) findViewById(R.id.service1);
        service2 = (TextView) findViewById(R.id.service2);
        service3 = (TextView) findViewById(R.id.service3);
        service4 = (TextView) findViewById(R.id.service4);
        Intent intent = getIntent();
//        String mac = intent.getStringExtra("macAddr");
        String devName = intent.getStringExtra("name");
        name.setText(devName);
        getMessage();
    }

    private void getMessage() {
        StringBuilder builder = new StringBuilder();
        ArrayList<BluetoothGattService> serviceArrayList = new ArrayList<>();
        BluetoothGatt gatt = BLEControl.get().getBluetoothGatt();
        if (gatt == null) {
            textView.setText("gatt == null");
            return;
        }
        serviceArrayList = (ArrayList<BluetoothGattService>) gatt.getServices();
        LogUtil.e("Gatt getServices size" + serviceArrayList.size());
        for (int i = 0; i < serviceArrayList.size(); i++) {

            BluetoothGattService service = serviceArrayList.get(i);
            UUID uuid = service.getUuid();
            builder.append("服务UUID:").append(uuid.toString());    //服务的UUID
            List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = service.getCharacteristics();
            LogUtil.e("Gatt  BluetoothGattCharacteristic size:" + bluetoothGattCharacteristics.size());
            for (int i1 = 0; i1 < bluetoothGattCharacteristics.size(); i1++) {
                StringBuilder builder1 = new StringBuilder();
                BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristics.get(i1);
                UUID uuid1 = characteristic.getUuid();//特性的UUID
                builder1.append(uuid1.toString() + "支持");
                int properties = characteristic.getProperties();    //用于区分特性用途（读、写、通知）
                if ((properties & PROPERTY_READ) != 0) {
                    builder1.append("读");
                }
                if ((properties & PROPERTY_WRITE) != 0) {
                    builder1.append("写");
                }
                if ((properties & PROPERTY_NOTIFY) != 0) {
                    builder1.append("通知");
                }
                LogUtil.e(builder1.toString());
            }
            textView.setText(builder.toString());

            //TODO 转成listview显示
            if (i == 0) {
                service1.setText(uuid.toString());
            } else if (i == 1) {
                service2.setText(uuid.toString());
            } else if (i == 2) {
                service3.setText(uuid.toString());
            } else {
                service4.setText(uuid.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BLEControl.get().disConnect();
    }
}
