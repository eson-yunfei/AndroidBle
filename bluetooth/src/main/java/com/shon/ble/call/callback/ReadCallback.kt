package com.shon.ble.call.callback

interface ReadCallback:BleCallback<ByteArray>{
     fun onExecute()
}