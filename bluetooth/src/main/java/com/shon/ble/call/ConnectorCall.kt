package com.shon.ble.call

import com.shon.ble.call.callback.ConnectCallback

class ConnectorCall(address: String) :
    BleCall<ConnectCallback>(address)