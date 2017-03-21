package com.e.ble.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.text.TextUtils;

import com.e.ble.BLESdk;
import com.e.ble.bean.BLEDevice;
import com.e.ble.check.BLECheck;
import com.e.ble.util.BLEError;
import com.e.ble.util.BLELog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @作者 xiaoyunfei
 * @日期: 2017/2/22
 * @说明： 蓝牙扫描工具类
 * <p>
 * |只提供 开始扫描&停止扫描
 * <p>
 * |---------------------------------------------------------------------------------------------------------------|
 */

public class BLEScanner implements BLEScanListener {

	public static final int INFINITE = -1;// 无限时长扫描，用户手动调用停止扫描
	public static final int DEFAULT = 0;//默认时长
	private static BLEScanner bleScanner = null;

	private BluetoothAdapter bluetoothAdapter;

	private int scanTime = 10000;            //扫描时长
	private String[] nameFilter = null;        //名称过滤
	private BLEScanListener bleScanListener;    //扫描监听
	private BLEDevice bleDevice = null;


	// |---------------------------------------------------------------------------------------------------------------|
	// |初始化操作
	// |---------------------------------------------------------------------------------------------------------------|
	private BLEScanner() {

	}

	public static void init() {
		if (bleScanner == null) {
			bleScanner = new BLEScanner();
		}
		BLELog.i("BLEScanner init ok");
	}

	public static BLEScanner get() {
		if (bleScanner == null) {
			init();
		}
		return bleScanner;
	}

	/**
	 * |---------------------------------------------------------------------------------------------------------|
	 * <p>
	 * |
	 * <p>
	 * |   提供给外部调用的方法
	 * <p>
	 * |
	 * <p>
	 * |--------------------------------------------------------------------------------------------------------|
	 */

	/**
	 * 停止扫描设备
	 * <p>
	 * 回调扫描结束
	 */
	public void stopScan() {

		BLELog.d("BLEScanner :: stopScan ()");
		//停止扫描设备
		if (bluetoothAdapter != null) {
			bluetoothAdapter.stopLeScan(scanCallback);
		}

		//取消定时
		stopScannerTimer();

	}


	/**
	 * 开始扫描设备
	 * <p>
	 * 自定义时长，添加名称过滤，UUID 过滤
	 * <p>
	 * 扫描时，不考虑是否正在扫描，
	 * <p>
	 * 直接尝试停止之前的扫描，然后重新进行扫描
	 *
	 * @param timeOut         ：扫描超时设置
	 * @param nameFilter      ：名称过滤  ，只显示指定名称的设备
	 * @param uuidFilter      ：uuID过滤	，只显示指定UUID的设备
	 * @param bleScanListener ：扫描回调
	 */
	public void startScan(int timeOut, String[] nameFilter, UUID[] uuidFilter, BLEScanListener bleScanListener) {

		BLELog.d("BLEScanner :: startScan ()");
		deviceMac.clear();

		this.bleScanListener = bleScanListener;

		if (!BLECheck.get().isBleEnable()) {
			onScannerError(BLEError.BLE_CLOSE);
			return;
		}

		stopScan();    //停止扫描
		this.nameFilter = nameFilter;

		if (bluetoothAdapter == null) {
			bluetoothAdapter = BLESdk.get().getBluetoothAdapter();
		}

		onScannerStart();

		bluetoothAdapter.startLeScan(uuidFilter, scanCallback);

		if (timeOut == INFINITE) {
			//无限扫描
			return;
		}
		scanTime = timeOut;

		if (timeOut == DEFAULT) {
			scanTime = 10000;
		}

		startTimer();
	}


	private Handler handler = null;

	/**
	 * 开始扫描时长倒计时
	 * <p>
	 * 开始之前先尝试停止上一次的倒计时
	 */
	private void startTimer() {
		stopScannerTimer();
		handler = new Handler();
		handler.postDelayed(stopScanRunnable, scanTime);
	}


	/**
	 * 定时需要执行的任务
	 * <p>
	 * 停止设备扫描
	 */
	Runnable stopScanRunnable = new Runnable() {
		@Override
		public void run() {

			stopScan();

			//回调扫描结束状态
			onScannerStop();

		}
	};

	/**
	 * 结束扫描定时
	 */
	private void stopScannerTimer() {
		if (handler != null) {
			handler.removeCallbacks(stopScanRunnable);
			handler = null;
		}
	}

	//内部名称过滤
	private List<String> deviceMac = new ArrayList<>();

	/**
	 * 系统的设备扫描回调监听
	 * <p>
	 * SDK 加入了重复地址过滤，
	 * <p>
	 * 用户不需要再次过滤
	 */
	private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

			if (bleScanListener == null) {
				return;
			}

			bleDevice = getBleDevice(device);
			if (bleDevice == null) {
				return;
			}
			bleDevice.setRssi(rssi);

			//返回设备
			onScanning(bleDevice);
		}
	};

	/**
	 * 根据 BluetoothDevice  获取 BLEDevice
	 *
	 * @param device
	 *
	 * @return
	 */
	private BLEDevice getBleDevice(BluetoothDevice device) {

		String name = device.getName();

		//获取设备名称，名称有可能为： 空字符
		if (TextUtils.isEmpty(name)) {
			name = "< UnKnow >";
		}
		//判断是否在名称过滤范围之内
		//如果不包含，不添加设备
		if (!containName(name)) {
			return null;
		}

		String mac = device.getAddress();

		BLELog.i("BLEScanner :: scanCallback()"
				+ "\ndevice "
				+ "\nname-->>" + name
				+ "\naddress-->>" + mac);
//		//过滤设备MAC 地址，此处为去重
//		if (isAddDevice(mac)) {
//			return null;
//		}
//
//		deviceMac.add(mac);

		// BLEDevice
		BLEDevice bleDevice = new BLEDevice();
		bleDevice.setName(name);
		bleDevice.setMac(device.getAddress());
		return bleDevice;
	}

	/**
	 * 是否已加入的列表中
	 *
	 * @param mac
	 *
	 * @return
	 */
	private boolean isAddDevice(String mac) {

		if (deviceMac == null || deviceMac.size() == 0) {
			return false;
		}
		boolean isAdd = false;
		for (String s : deviceMac) {
			if (s.equals(mac)) {
				isAdd = true;
				break;
			}
		}
		return isAdd;
	}


	/**
	 * 检测设备名称是否在过滤名称范围内
	 *
	 * @param name
	 *
	 * @return 如果包含设备名称，返回true
	 */
	private boolean containName(String name) {

		if (nameFilter == null || nameFilter.length == 0) {
			return true;
		}
		boolean isContain = false;
		for (int i = 0; i < nameFilter.length; i++) {

			if (name.equals(nameFilter[i])) {
				isContain = true;
			}
			break;
		}

		return isContain;
	}


	@Override
	public void onScannerStart() {
		if (bleScanListener != null) {
			bleScanListener.onScannerStart();
		}
	}

	@Override
	public void onScanning(BLEDevice device) {
		bleScanListener.onScanning(device);
	}

	@Override
	public void onScannerStop() {
		if (bleScanListener != null) {
			bleScanListener.onScannerStop();
		}
	}

	@Override
	public void onScannerError(int errorCode) {
		if (bleScanListener != null) {
			bleScanListener.onScannerError(errorCode);
		}
	}
}
