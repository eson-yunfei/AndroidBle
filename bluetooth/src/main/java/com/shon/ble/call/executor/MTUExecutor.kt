package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.shon.ble.call.data.MTUDataMessage
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class MTUExecutor(
    private val gatt: BluetoothGatt?,
    private val mtu: Int
) : IEventBus<Boolean, MTUDataMessage>() {

    @SuppressLint("MissingPermission")
    override suspend fun execute(): Boolean? {
        if (gatt == null) {
            setResult(false)
        } else {
            gatt.requestMtu(mtu)
        }
        return channel.receive()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun receiverMessage(message: MTUDataMessage) {
        super.receiverMessage(message)
        if (message.status == BluetoothGatt.GATT_SUCCESS) {
            setResult(true)
        } else {
            setResult(false)
        }
    }
}