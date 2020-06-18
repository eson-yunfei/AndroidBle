package org.eson.liteble.activity;

import android.os.Bundle;

import com.e.ble.util.BLEByteUtil;
import com.e.ble.util.BLEConstant;

import org.eson.liteble.RxBus;
import org.eson.liteble.bean.BleDataBean;
import org.eson.liteble.util.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/27
 * @说明：
 */

public class BaseBleActivity extends BaseActivity {

    protected CompositeDisposable compositeDisposable;

    @Override
    protected int getRootLayout() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRxReceive();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (compositeDisposable != null && compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }

    private void initRxReceive() {

        if (compositeDisposable != null) {
            return;
        }
        compositeDisposable = new CompositeDisposable();

        RxBus.getInstance().toObserverable()
                .map(new Function<Object, Bundle>() {
                    @Override
                    public Bundle apply(Object o) throws Exception {
                        return (Bundle) o;
                    }
                }).subscribe(new Observer<Bundle>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Bundle value) {

                boolean containsBleStateKey = value.containsKey(BLEConstant.Type.TYPE_STATE);

                if (containsBleStateKey) {
                    //蓝牙状态改变
                    final int state = value.getInt(BLEConstant.Type.TYPE_STATE, 0);
                    final String mac = value.getString(BLEConstant.Type.TYPE_NAME);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changerBleState(mac, state);
                        }
                    });

                }


                boolean containBleDataKey = value.containsKey(BLEConstant.Type.TYPE_NOTICE);
                if (containBleDataKey) {
                    //蓝牙数据返回
                    BleDataBean dataBean = (BleDataBean) value.getSerializable(BLEConstant.Type.TYPE_NOTICE);

                    if (dataBean == null) {
                        return;
                    }
                    final String uuid = dataBean.getUuid().toString();
                    final byte[] buffer = dataBean.getBuffer();
                    final String deviceAddress = dataBean.getDeviceAddress();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeBleData(uuid, buffer, deviceAddress);
                        }
                    });

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    protected void changeBleData(String uuid, byte[] buffer, String deviceAddress) {

        ToastUtil.showShort(mContext, "uuid:"
                + uuid + "\ndata:" + BLEByteUtil.getHexString(buffer)
                + "\ndeviceAddress:" + deviceAddress);
    }

    protected void changerBleState(String mac, int state) {

    }
}
