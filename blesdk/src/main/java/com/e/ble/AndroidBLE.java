/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.e.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.e.ble.util.BLELog;


/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： AndroidBLE  SDK 内部访问的 类
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */
class AndroidBLE {
    private static volatile AndroidBLE androidBLE;

    static {
        androidBLE = null;
    }

    private Context context;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    // |---------------------------------------------------------------------------------------------------------------|
    //初始化操作，不说了
    private AndroidBLE(Context context) {
        this.context = context;
    }

    public static void init(Context context) {

        if (androidBLE != null) {
            return;
        }
        synchronized (AndroidBLE.class) {
            if (androidBLE == null) {
                androidBLE = new AndroidBLE(context);
                BLELog.i("AndroidBLE is init ok");
            }
        }

    }

    public static AndroidBLE get() throws NullPointerException {
        if (androidBLE == null) {
            throw new NullPointerException("AndroidBLE not init");
        }
        return androidBLE;
    }
// |---------------------------------------------------------------------------------------------------------------------|

    /**
     * 获取系统 BluetoothManager
     *
     * @return
     */
    public synchronized BluetoothManager getBluetoothManager() {
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }
        return bluetoothManager;
    }

    /**
     * 获取系统 BluetoothAdapter
     *
     * @return
     */
    public synchronized BluetoothAdapter getBluetoothAdapter() throws NullPointerException {
        getBluetoothManager();
        if (bluetoothManager == null) {

            BLELog.e("AndroidBLE.java------->>>bluetoothManager is null");
            throw new NullPointerException("AndroidBLE.java : getBluetoothAdapter() : bluetoothManager is null");
        }
        if (bluetoothAdapter == null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        return bluetoothAdapter;


    }

    /**
     * 重置 bluetoothManager ， bluetoothAdapter
     */
    public void reset() {
//		bluetoothAdapter = null;
//		bluetoothManager = null;

    }
}
