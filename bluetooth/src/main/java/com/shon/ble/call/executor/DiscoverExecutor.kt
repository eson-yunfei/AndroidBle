package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.text.TextUtils
import com.shon.ble.call.callback.DiscoverCallback
import com.shon.ble.call.data.DiscoverMessage
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class DiscoverExecutor(
    private val address: String,
    private val gatt: BluetoothGatt,
    private val discoverCallback: DiscoverCallback
) :
    BaseExecutor<DiscoverCallback, DiscoverMessage>(discoverCallback) {
    @SuppressLint("MissingPermission")
    override fun execute() {
        super.execute()
        gatt.discoverServices()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun onReceiverMessage(message: DiscoverMessage) {
        message.gatt?.let {
            val mac = it.device.address
            if (TextUtils.equals(address, mac)) {
                super.onReceiverMessage(message)
                if (message.status == BluetoothGatt.GATT_SUCCESS) {
                    discoverCallback.onResult(true)
                } else {
                    discoverCallback.onResult(false)
                }

            }
        }


    }
}