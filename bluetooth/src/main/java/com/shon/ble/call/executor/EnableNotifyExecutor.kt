package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.text.TextUtils
import com.shon.ble.call.data.EnableNotifyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

internal class EnableNotifyExecutor(
    private val gatt: BluetoothGatt?,
    private val address: String,
    private val descriptorUUID: String,
    private val
    gattCharacteristic: BluetoothGattCharacteristic?
) : IEventBus<Boolean, EnableNotifyState>() {

    @SuppressLint("MissingPermission")
    override suspend fun execute(): Boolean? {

        if (gatt == null || gattCharacteristic == null) {
//            channel.send(false)
            return false
        }
        gatt.setCharacteristicNotification(gattCharacteristic,true)
        val descriptor = gattCharacteristic.getDescriptor(UUID.fromString(descriptorUUID))
        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt.writeDescriptor(descriptor)
        return channel.receive()
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun receiverMessage(message: EnableNotifyState) {

        if (message.status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        val mac = message.gatt?.device?.address

        if (TextUtils.equals(address, mac)) {
            CoroutineScope(Dispatchers.IO).launch {
                channel.send(true)
                super.receiverMessage(message)
            }
        }
    }
}