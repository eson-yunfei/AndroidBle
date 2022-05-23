package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.text.TextUtils
import com.shon.ble.call.callback.ReadCallback
import com.shon.ble.call.data.ReadDataMessage
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


internal class ReadExecutor(
    private val address: String,
    private val gatt: BluetoothGatt,
    private val gattCharacteristic: BluetoothGattCharacteristic,
    private val readCallback: ReadCallback
) : BaseExecutor<ReadCallback, ReadDataMessage>(readCallback) {

    @SuppressLint("MissingPermission")
    override fun execute() {
        super.execute()
        readCallback.onExecute()
        gatt.readCharacteristic(gattCharacteristic)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun onReceiverMessage(message: ReadDataMessage) {
        val status = message.status
        val gattResult = message.gatt
        val characteristic = message.characteristic
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        if (gattResult == null || characteristic == null) {
            return
        }
        val mac = gatt.device.address

        if (!TextUtils.equals(address, mac)) {
            return
        }

        val mUuid: String = gattCharacteristic.uuid.toString().lowercase(Locale.getDefault())
        val uuid: String = characteristic.uuid.toString().lowercase(Locale.getDefault())

        if (!TextUtils.equals(mUuid, uuid)) {
            return
        }
        super.onReceiverMessage(message)
        val value: ByteArray = characteristic.value
        readCallback.onResult(value)

    }

}
