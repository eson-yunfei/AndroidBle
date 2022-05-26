package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.text.TextUtils
import com.shon.ble.call.callback.SendCallback
import com.shon.ble.call.callback.WriteCallback
import com.shon.ble.call.data.WriteDataMessage
import com.shon.ble.call.data.WriteResultMessage
import com.shon.ble.call.data.WriteWithoutResult
import com.shon.ble.util.BleLog
import com.shon.ble.util.ByteUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


internal class WriterExecutor<T>(
    private val address: String,
    private val gatt: BluetoothGatt,
    private val gattCharacteristic: BluetoothGattCharacteristic,
    private val sendCallback: SendCallback<T>,
    private val writeCallback: WriteCallback
) : BaseExecutor<WriteDataMessage>(writeCallback) {

    private val sendData = sendCallback.getSendData()
    private val needResult = sendCallback.needResult()

    @SuppressLint("MissingPermission")
    override fun execute() {
        super.execute()
        BleLog.d("start write data :$sendData")
        val hexStringToByte = ByteUtil.hexStringToByte(sendData)
        val setValue = gattCharacteristic.setValue(hexStringToByte)
        if (setValue) {
            gatt.writeCharacteristic(gattCharacteristic)
        } else {
            writeCallback.onResult(false)
            unregisterReceiver()
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun onReceiverMessage(message: WriteDataMessage) {

        when (message) {
            is WriteWithoutResult -> {
                val onCharacteristicWrite =
                    onCharacteristicWrite(message.gatt, message.characteristic, message.status)
                if (onCharacteristicWrite) {
                    //是当前的发送的指令返回数据
                    if (!needResult) {
                        super.onReceiverMessage(message)
                        writeCallback.onResult(true)
                    }
                }
            }
            is WriteResultMessage -> {
                if (!TextUtils.equals(message.address, address)) {
                    return
                }
                val processResult = sendCallback.onProcess(message.data)
                processResult?.let {
                    if (sendCallback.receiveFinish()) {
                        super.onReceiverMessage(message)
                        val convertResult = sendCallback.convertResult(it)
                        sendCallback.onResult(convertResult)
                        writeCallback.onResult(true)
                    }
                }
            }
        }

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
        val deviceAddress = gatt.device.address
        if (!TextUtils.equals(address, deviceAddress)) {
            return false
        }

        val value = characteristic.value ?: return false

        val setValue = ByteUtil.getHexString(value).lowercase()
        if (!TextUtils.equals(setValue, sendData.lowercase())) {
            return false
        }
        return true
    }
}
