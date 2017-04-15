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

package com.e.ble.util;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/7
 * @说明：
 */

public interface BLEError {

    int BLE_CLOSE = 1001;       //蓝牙已关闭
    int BLE_OUT_MAX_CONNECT = 1002; //超过最大连接数


    int BLE_SCANNER_CALLBACK_NULL = 1003;   //扫描回调为null

    int BLE_DEVICE_ERROR = 1004;        //设备或手机异常
}
