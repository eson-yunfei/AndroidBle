package com.shon.ble.call.executor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.text.TextUtils
import com.shon.ble.util.BleLog
import com.shon.ble.BleManager
import com.shon.ble.call.callback.ConnectCallback
import com.shon.ble.call.data.ConnectionState
import com.shon.ble.data.ConnectResult
import com.shon.ble.gatt.BleGattCallback
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class ConnExecutor(
    private val address: String,
    private val manager: BluetoothManager,
    private val connectCallback: ConnectCallback
) : IEventBus<ConnectResult, ConnectionState>() {

    private var timeoutJob: Job? = null

    @SuppressLint("MissingPermission")
    override suspend fun execute(): ConnectResult? {

        val adapter = manager.adapter
        if (!adapter.isEnabled) {
            BleLog.e("蓝牙不可用，请先开启蓝牙")
            return ConnectResult.ConnectError(address, -1)
        }
        val remoteDevice = adapter.getRemoteDevice(address)
        remoteDevice.connectGatt(
            BleManager.getManager().context,
            false, BleGattCallback.bleGattCallback,
            BluetoothDevice.TRANSPORT_LE
        )

        timeoutJob = createTimeoutJob()
        return channel.receive()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    override fun receiverMessage(message: ConnectionState) {
        super.receiverMessage(message)
        onConnectionStateChange(
            message.gatt,
            message.status, message.newState
        )
    }

    @SuppressLint("MissingPermission")
    private fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        BleLog.d("ConnExecutor  onConnectionStateChange :: status = $status ; newState = $newState")
        val device = gatt?.device
        val mac = device?.address
        if (!TextUtils.equals(address, mac)) {
            return
        }
        if (status == 133) {
            gatt?.disconnect()
            gatt?.disconnect()
            gatt?.close()
            setResult(ConnectResult.ConnectError(address, -1))
            timeoutJob?.cancel()
            return
        }
        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                setResult(ConnectResult.ConnectSuccess(address, gatt!!))
                timeoutJob?.cancel()
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                gatt?.close()
                setResult(ConnectResult.Disconnect(address))
                timeoutJob?.cancel()
            }
        }
    }

    private fun createTimeoutJob(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            delay(connectCallback.timeout())
            BleLog.e("设备连接超时")
            setResult(ConnectResult.ConnectTimeout(address))
        }
    }
}