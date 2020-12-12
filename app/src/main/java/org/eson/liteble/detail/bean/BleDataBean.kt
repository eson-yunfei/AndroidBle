package org.eson.liteble.detail.bean

import org.eson.liteble.common.util.TimeUtils
import java.util.*

/**
 * @作者 xiaoyunfei
 * @日期: 2017/3/8
 *
 * update on 2020/12/12 by xiao yun fei
 * @说明：
 */
data class BleDataBean(
        val deviceAddress: String,
        val uuid: String,
        val buffer: ByteArray,
        val time: String =TimeUtils.getCurrentTime()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BleDataBean

        if (deviceAddress != other.deviceAddress) return false
        if (uuid != other.uuid) return false
        if (!buffer.contentEquals(other.buffer)) return false
        if (time != other.time) return false

        return true
    }

    override fun hashCode(): Int {
        var result = deviceAddress.hashCode()
        result = 31 * result + uuid.hashCode()
        result = 31 * result + buffer.contentHashCode()
        result = 31 * result + (time?.hashCode() ?: 0)
        return result
    }
}
