package com.shon.ble.listener

import com.shon.ble.call.data.WriteResultMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class BleDataListenerExecutor private constructor() {
    companion object {
        val listenerExecutor by lazy { BleDataListenerExecutor() }
    }

    private val dataChannel: Channel<WriteResultMessage> = Channel(Int.MAX_VALUE)

    private var listener: BleDataListener? = null

    init {
        EventBus.getDefault().register(this)
        dispatchMessage()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun receiverData(dataMessage: WriteResultMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            dataChannel.send(dataMessage)
        }
    }

    fun setDataListener(bleDataListener: BleDataListener) {
        listener = bleDataListener
    }


    private fun dispatchMessage() {
        CoroutineScope(Dispatchers.IO).launch {
           while (true){
               val receive = dataChannel.receive()
               listener?.let {
                   val address = receive.address
                   val data = receive.data
                   it.onReceiverData(address, data)
               }
           }
        }
    }
}