package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.text.TextUtils
import com.shon.ble.call.callback.EnableNotifyCallback
import com.shon.ble.call.data.EnableNotifyState
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 启用 notify
 */
internal class EnableNotifyExecutor(
    private val address: String,
    private val gatt: BluetoothGatt,
    private val descriptorUUID: String,
    private val gattCharacteristic: BluetoothGattCharacteristic,
    private val enableNotifyCallback: EnableNotifyCallback,
) : BaseExecutor<EnableNotifyState>(enableNotifyCallback) {

    @SuppressLint("MissingPermission")
    override fun execute() {
        super.execute()
        gatt.setCharacteristicNotification(gattCharacteristic, true)
        val descriptor = gattCharacteristic.getDescriptor(UUID.fromString(descriptorUUID))
        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt.writeDescriptor(descriptor)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun onReceiverMessage(message: EnableNotifyState) {
        if (message.status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        val mac = message.gatt?.device?.address
        if (TextUtils.equals(address, mac)) {
            super.onReceiverMessage(message)
            enableNotifyCallback.onResult(true)
        }
    }
}