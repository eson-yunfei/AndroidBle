package com.shon.ble.call

import android.bluetooth.BluetoothGatt
import com.shon.ble.call.callback.MTUCallback

class MTUCall(address:String,val gatt: BluetoothGatt,val mtu:Int):BleCall<MTUCallback>(address)