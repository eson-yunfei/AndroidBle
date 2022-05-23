package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.text.TextUtils
import com.shon.ble.call.data.ReadDataMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

internal class ReadExecutor(
    private val address: String,
    private val gatt: BluetoothGatt?,
    private val gattCharacteristic: BluetoothGattCharacteristic?,
) : IEventBus<ByteArray, ReadDataMessage>() {


    @SuppressLint("MissingPermission")
    override suspend fun execute(): ByteArray? {
        if (gatt == null || gattCharacteristic == null) {
            channel.send(null)
            channel.close()
            return channel.receive()
        }

        gatt.readCharacteristic(gattCharacteristic)

        return channel.receive()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun receiverMessage(message: ReadDataMessage) {
        super.receiverMessage(message)
        CoroutineScope(Dispatchers.IO).launch {
            onCharacteristicRead(
                message.gatt, message.characteristic,
                message.status
            )
        }
    }

    private suspend fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            channel.send(null)
            channel.close()
            return
        }
        if (gatt == null || characteristic == null) {
            channel.send(null)
            channel.close()
            return
        }
        val mac = gatt.device.address

        if (!TextUtils.equals(address, mac)) {
            return
        }

        val mUuid: String = gattCharacteristic?.uuid.toString().lowercase(Locale.getDefault())
        val uuid: String = characteristic.uuid.toString().lowercase(Locale.getDefault())

        if (!TextUtils.equals(mUuid, uuid)) {
            return
        }
        val value: ByteArray = characteristic.value
        channel.send(value)
        channel.close()
    }
}