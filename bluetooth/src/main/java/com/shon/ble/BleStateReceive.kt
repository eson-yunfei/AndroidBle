package com.shon.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.lang.ref.WeakReference


open class BleStateReceive(private val mContext: WeakReference<Context>) : BroadcastReceiver() {

    fun registerBleReceive() {
        mContext.get()?.registerReceiver(this, getIntentFilter())
    }

    fun unRegisterBleReceive() {
        mContext.get()?.unregisterReceiver(this)
    }

    private fun getIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF")
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON")
        return intentFilter
    }

    override fun onReceive(cxt: Context?, intent: Intent?) {
        intent ?: return
        when (intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) as? BluetoothDevice

                device ?: return
                onDeviceConnect(device)
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) as? BluetoothDevice

                device ?: return
                onDeviceDisConnect(device)
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    BluetoothAdapter.STATE_OFF -> onStateChange(false)
                    BluetoothAdapter.STATE_ON -> onStateChange(true)
                }
            }
        }
    }

    open fun onDeviceConnect(device: BluetoothDevice) {}
    open fun onDeviceDisConnect(device: BluetoothDevice) {}
    open fun onStateChange(open: Boolean) {}
}