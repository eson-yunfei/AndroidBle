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

package com.e.ble.control.listener;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/5
 * @说明： 设备连接状态接口
 * <p>
 * 包括设备的连接
 */

public interface BLEConnListener {

    void onConnError(String address, int errorCode);//连接设备异常

    void onConnSuccess(String address);//设备连接成功,设备服务 Discovered

    void onAlreadyConnected(String address);//设备已经连接

}
