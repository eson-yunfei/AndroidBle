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

package com.e.ble.scan;


import com.e.ble.bean.BLEDevice;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明：
 */
public abstract class BLEScanListener {

	public  void onScannerStart(){

	}

	/**
	 *
	 * @param device
	 */
	public abstract void onScanning(BLEDevice device);

	public void onScannerStop(){

	}

	public void onScannerError(int errorCode){

	}

	public boolean isRemove(){
		return true;
	}
}
