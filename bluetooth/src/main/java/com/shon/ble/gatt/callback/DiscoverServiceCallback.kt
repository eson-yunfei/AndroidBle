package com.shon.ble.gatt.callback

import android.bluetooth.BluetoothGatt
import android.text.TextUtils

abstract class DiscoverServiceCallback(private val address:String) {

   fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int){
      if (status != BluetoothGatt.GATT_SUCCESS) {
         return
      }
      val device = gatt?.device
      if (TextUtils.equals(address,device?.address)){
         discovered()
      }
   }

   abstract fun discovered()
}