package org.eson.liteble.logger

import org.eson.liteble.ext.getCurrentTime

data class LogMessageBean(
    val address: String, val title: String, val content: String,
    val createTime: String = getCurrentTime(),
)