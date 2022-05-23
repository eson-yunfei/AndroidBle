package com.shon.ble.call

import android.bluetooth.BluetoothGatt
import com.shon.ble.call.callback.DiscoverCallback

class DiscoverCall(address:String,val gatt: BluetoothGatt):BleCall<DiscoverCallback>(address)