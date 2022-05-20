package org.eson.liteble.main

import android.bluetooth.BluetoothGatt
import android.text.TextUtils
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shon.bluetooth.core.Connect
import com.shon.bluetooth.core.ConnectCallback
import com.shon.bluetooth.core.call.ReadCall
import com.shon.bluetooth.core.callback.ReadCallback
import com.shon.bluetooth.util.ByteUtil
import no.nordicsemi.android.support.v18.scanner.ScanResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val selectDevice: MutableState<ScanResult?> = mutableStateOf(null)

class ConnectViewModel : ViewModel() {

    val connectedState: MutableState<String> = mutableStateOf("DisConnect")
    val connectedGatt: MutableState<BluetoothGatt?> = mutableStateOf(null)

    val showLogWindow:MutableState<Boolean> = mutableStateOf(false)
    fun startConnectDevice() {

        selectDevice.value?.let {
            Connect(it.device.address).enqueue(object : ConnectCallback() {
                override fun onConnectSuccess(address: String?, gatt: BluetoothGatt?) {
                    connectedState.value = "Connected"
                }

                override fun onConnectError(address: String?, errorCode: Int) {
                    connectedState.value = "Connect Error"
                }

                override fun onServiceEnable(address: String?, gatt: BluetoothGatt?) {
                    connectedGatt.value = gatt
                }

                override fun onDisconnected(address: String?) {
                    connectedState.value = "DisConnect"
                }

            })
        }
    }


    private fun loadBleGattInfo(gatt: BluetoothGatt) {

    }

    suspend fun readInfo(serviceUUID: String, characterUUID: String): String? {
        return suspendCoroutine { coroutine ->
            selectDevice.value ?: kotlin.run { coroutine.resume(null) }
            selectDevice.value?.let {
                ReadCall(it.device.address)
                    .setServiceUUid(serviceUUID)
                    .setCharacteristicUUID(characterUUID)
                    .enqueue(object : ReadCallback() {
                        override fun process(address: String?, result: ByteArray?): Boolean {
                            if (TextUtils.equals(address, it.device.address)) {
                                val text =
                                    ByteUtil.getHexString(result) + " (" + ByteUtil.byteToCharSequence(
                                        result
                                    ) + ")"
                                coroutine.resume(text)
                                return true
                            }
                            return false
                        }

                        override fun onTimeout() {
                            coroutine.resume(null)
                        }

                    })
            }

        }

    }

}