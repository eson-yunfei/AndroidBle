package com.shon.ble.call.executor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

internal abstract class IEventBus<T, M> {
    internal val channel = Channel<T?>()

    init {
        EventBus.getDefault().register(this)
    }


    open fun setResult(result: T?) {
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                channel.send(result)
                channel.close()
            }
        }
    }

    open fun receiverMessage(message: M) {
        EventBus.getDefault().unregister(this)
    }

    abstract suspend fun execute(): T?
}