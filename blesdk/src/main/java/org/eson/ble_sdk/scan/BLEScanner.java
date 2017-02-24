package org.eson.ble_sdk.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.text.TextUtils;

import org.eson.ble_sdk.BLESdk;
import org.eson.ble_sdk.bean.BLEDevice;
import org.eson.ble_sdk.util.BLELog;

import java.util.UUID;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 蓝牙扫描工具类
 */

public class BLEScanner {

	private BluetoothAdapter bluetoothAdapter;

	private int scanTime = 10000;            //扫描时长
	private String[] nameFilter = null;        //名称过滤
	private BLEScanListener bleScanListener;    //扫描监听
	private BLEDevice bleDevice = null;

	private BLEScanner() {

	}

	private static BLEScanner bleScanner = null;

	public static void init() {
		if (bleScanner == null) {
			bleScanner = new BLEScanner();
		}
		BLELog.i("BLEScanner init ok");
	}

	public static BLEScanner get() {
		init();
		return bleScanner;
	}

	/**
	 * 开始扫描
	 *
	 * @param timeOut         ：扫描超时设置
	 * @param nameFilter      :名称过滤
	 * @param uuidFilter      :uuID过滤
	 * @param bleScanListener ：扫描回调
	 */
	public void startScan(int timeOut, String[] nameFilter, UUID[] uuidFilter, BLEScanListener bleScanListener) {


		this.nameFilter = nameFilter;
		this.bleScanListener = bleScanListener;
		if (bluetoothAdapter == null) {
			bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
		}

		if (bleScanListener != null) {
			bleScanListener.onScannerStart();
		}

		bluetoothAdapter.startLeScan(uuidFilter, scanCallback);


		//timeOut 制定了两个特殊值：
		// -1  --->>  无限时长扫描，手动停止
		// 0  ---->> 设置默认值，
		if (timeOut == -1) {
			//无限扫描
		} else {
			if (timeOut == 0) {
				//
				scanTime = 10000;
			} else {
				scanTime = timeOut;
			}

			startTimer();
		}

	}

	/**
	 * 停止扫描设备
	 */
	public void stopScan() {
		stopScannerTimer();
		if (bluetoothAdapter == null) {
			return;
		}
		bluetoothAdapter.stopLeScan(scanCallback);
	}


	private Handler handler = null;

	/**
	 * 开始计时
	 */
	private void startTimer() {

		stopScannerTimer();
		handler = new Handler();
		handler.postDelayed(stopScanRunnable, scanTime);

	}


	/**
	 *
	 */
	Runnable stopScanRunnable = new Runnable() {
		@Override
		public void run() {

			callBackScanStop();

			stopScan();
		}
	};

	/**
	 * 回调扫描结束
	 */
	private void callBackScanStop() {
		if (bleScanListener == null) {
			return;
		}
		bleScanListener.onScannerStop();
	}

	/**
	 * 结束扫描定时
	 */
	private void stopScannerTimer() {
		if (handler != null) {
			handler.removeCallbacks(stopScanRunnable);
			handler = null;
		}
	}

	/**
	 *
	 */
	BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

			String name = device.getName();

			if (TextUtils.isEmpty(name)) {
				return;
			}
			if (containName(name)) {
				return;
			}

			bleDevice = new BLEDevice();
			bleDevice.setName(name);
			bleDevice.setMac(device.getAddress());
			bleDevice.setRssi(rssi);
			if (bleScanListener == null) {
				return;
			}
			bleScanListener.onScanning(bleDevice);

		}
	};


	/**
	 * 检测设备名称是否在过滤名称范围内
	 *
	 * @param name
	 *
	 * @return
	 */
	private boolean containName(String name) {
		boolean isContain = false;

		if (nameFilter == null || nameFilter.length == 0) {
			return false;
		}

		for (int i = 0; i < nameFilter.length; i++) {

			if (name.equals(nameFilter[i])) {
				isContain = true;
			}
			break;
		}

		return isContain;
	}
}
