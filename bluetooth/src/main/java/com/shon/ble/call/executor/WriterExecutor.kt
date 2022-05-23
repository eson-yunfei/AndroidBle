package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.text.TextUtils
import com.shon.ble.util.BleLog
import com.shon.ble.util.ByteUtil
import com.shon.ble.call.callback.SendCallback
import com.shon.ble.call.data.WriteDataMessage
import com.shon.ble.call.data.WriteResultMessage
import com.shon.ble.call.data.WriteWithoutResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class WriterExecutor(
    private val mac: String,
    private val gatt: BluetoothGatt?,
    private val gattCharacteristic: BluetoothGattCharacteristic?,
    private val sendCallback: SendCallback<Any>
) : IEventBus<Boolean, WriteDataMessage>() {

    private var sendData: String? = null
    @SuppressLint("MissingPermission")
    override suspend fun execute(): Boolean? {

        if (gatt == null || gattCharacteristic == null) {
            return false
        }

        sendData = sendCallback.getSendData()
        BleLog.d("start write data :$sendData")
        val hexStringToByte = ByteUtil.hexStringToByte(sendData)
        val setValue = gattCharacteristic.setValue(hexStringToByte)
        if (setValue) {
            gatt.writeCharacteristic(gattCharacteristic)
        } else {
            setResult(false)
        }
        return channel.receive()
    }

    private fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ): Boolean {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return false
        }
        if (gatt == null || characteristic == null) {
            return false
        }
        val address = gatt.device.address
        if (!TextUtils.equals(address, mac)) {
            return false
        }

        val value = characteristic.value ?: return false

        val setValue = ByteUtil.getHexString(value).lowercase()
        if (!TextUtils.equals(setValue, sendData?.lowercase())) {
            return false
        }
        setResult(true)

        return true
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun receiverMessage(message: WriteDataMessage) {
//        BleLog.d("on receiverMessage = $message")
        when (message) {

            is WriteWithoutResult -> {

                val onCharacteristicWrite =
                    onCharacteristicWrite(message.gatt, message.characteristic, message.status)
                if (onCharacteristicWrite) {
                    if (!sendCallback.needResult()) {
                        super.receiverMessage(message)
                        sendCallback.onResult(true)
                    }
                }
            }
            is WriteResultMessage -> {
                if (!TextUtils.equals(message.address, mac)) {
                    return
                }
                val onProcess = sendCallback.onProcess(message.data)
                onProcess?.let {
                    if (sendCallback.receiveFinish()) {
                        setResult(true)
                        super.receiverMessage(message)
                        sendCallback.onResult(it)
                    }
                }
            }
        }
    }

    private fun setResult(result: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                channel.send(result)
                channel.close()
            }
        }
    }

}