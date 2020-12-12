package org.eson.liteble.common

import android.bluetooth.BluetoothGatt
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shon.bluetooth.core.Connect
import com.shon.bluetooth.core.ConnectCallback
import org.eson.liteble.detail.bean.BleDataBean
import java.util.*

class DeviceState private constructor() : ConnectCallback() {
    private val connectDevice: MutableList<DeviceLiveData>

    private val dataList: MutableList<BleDataBean>

     val dataListLiveData: MutableLiveData<List<BleDataBean>>


    companion object {

        @JvmStatic
        val instance: DeviceState by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DeviceState()
        }
    }

    init {
        connectDevice = ArrayList()
        dataList = mutableListOf()
        dataListLiveData = MutableLiveData()
    }


    @Synchronized
    fun addData(dataBean: BleDataBean) {
        if (dataList.size >= 500) {
            dataList.removeAt(dataList.size - 1)
        }

        dataList.add(0, dataBean)

        dataListLiveData.postValue(dataList)

    }

    fun getDeviceLiveData(address: String): DeviceLiveData? {
        return connectDevice.find {
            TextUtils.equals(it.deviceMac, address)
        }
    }

    fun connectDevice(address: String?, name: String?): DeviceLiveData {
        for (deviceLiveData in connectDevice) {
            if (TextUtils.equals(deviceLiveData.deviceMac, address)) {
                return deviceLiveData
            }
        }
        val deviceLiveData = DeviceLiveData(address, name)
        connectDevice.add(deviceLiveData)
        Connect(address)
                .setTimeout(5000)
                .setReTryTimes(2)
                .enqueue(instance)
        return deviceLiveData
    }

    override fun onConnectSuccess(address: String, gatt: BluetoothGatt) {
        for (deviceLiveData in connectDevice) {
            if (!TextUtils.equals(deviceLiveData.deviceMac, address)) {
                continue
            }
            deviceLiveData.onConnectSuccess(gatt)
        }
    }

    override fun onConnectError(address: String, errorCode: Int) {
        for (deviceLiveData in connectDevice) {
            if (!TextUtils.equals(deviceLiveData.deviceMac, address)) {
                continue
            }
            deviceLiveData.onConnectError(errorCode)
        }
    }

    override fun onServiceEnable(address: String, gatt: BluetoothGatt) {
        for (deviceLiveData in connectDevice) {
            if (!TextUtils.equals(deviceLiveData.deviceMac, address)) {
                continue
            }
            deviceLiveData.onServiceEnable(gatt)
        }
    }

    override fun onDisconnected(address: String) {
        for (deviceLiveData in connectDevice) {
            if (!TextUtils.equals(deviceLiveData.deviceMac, address)) {
                continue
            }
            deviceLiveData.onDisconnected()
        }
    }

    class DeviceLiveData(val deviceMac: String? = null, val deviceName: String? = null) : LiveData<DeviceLiveData?>() {


        var state = 0
        var errorCode = 0;
        var gatt: BluetoothGatt? = null

        fun onConnectSuccess(gatt: BluetoothGatt?) {
            state = STATE_CONNECTED
            this.gatt = gatt
            postValue(this)
        }

        fun onConnectError(errorCode: Int) {
            state = STATE_CONNECT_ERROR
            this.errorCode = errorCode;
            postValue(this)
        }

        fun onServiceEnable(gatt: BluetoothGatt?) {
            state = STATE_SERVER_ENABLE
            this.gatt = gatt

            postValue(this)
        }

        fun onDisconnected() {
            state = STATE_DIS_CONNECTED
            gatt = null
            postValue(this)
        }

        fun post(value: DeviceLiveData) {
            postValue(value)
        }

        companion object {
            const val STATE_CONNECT_ERROR = -1
            const val STATE_CONNECTED = 1
            const val STATE_SERVER_ENABLE = 2
            const val STATE_DIS_CONNECTED = 3
        }
    }


}