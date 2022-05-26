package org.eson.liteble

import com.shon.ble.call.callback.SendCallback
import com.shon.ble.util.BleLog
import com.shon.ble.util.ByteUtil

data class TestBean(
    val createTime: String,//yyyy-MM-dd HH:mm
    var temperature: Int,
    var humidity: Int
)

class TestHistoricalData : SendCallback<MutableList<TestBean>>() {
    private var  receiverFinished = false

    private val result: MutableList<Byte> = mutableListOf()
    override fun receiveFinish(): Boolean {
        return receiverFinished
    }
    override fun onResult(value: MutableList<TestBean>?) {
        value?.let { list ->
            list.forEach {
                BleLog.d("it = $it")
            }

        }
    }

    override fun onExecuted() {

    }

    override fun getSendData(): String {
        return "03"
    }

    override fun onProcess(data: ByteArray): ByteArray? {
        if (data[0] != 0x83.toByte()) {
            return null
        }
        if (data[2] == 1.toByte() || data[2] == 0xFF.toByte()) {
            receiverFinished = true
        }

        kotlin.runCatching {
            val packageCount = ByteUtil.cbyte2IntLow(byteArrayOf(data[3], data[4]), 0, 2)
            val currentPackage = ByteUtil.cbyte2IntLow(byteArrayOf(data[5], data[6]), 0, 2)
//            logD("packageCount = $packageCount ; currentPackage = $currentPackage")
            val percent: Int = currentPackage * 100 / packageCount
            BleLog.d("percent = $percent")
            val count = data.size - 7
            if (count > 0) {
                val tem = ByteArray(count)
                System.arraycopy(
                    data, 7,
                    tem, 0, count
                )
                result.addAll(tem.toList())
            }
        }
        return result.toByteArray()
    }

    override fun convertResult(result: ByteArray): MutableList<TestBean>? {
        val count = result.size / 9
        val mutableList: MutableList<TestBean> = mutableListOf()
        for (i in 0 until count) {
            val byteArray = ByteArray(9)
            System.arraycopy(
                result, i * 9,
                byteArray, 0, 9
            )
            val year = ByteUtil.cbyte2Int(byteArray[0]) + 2000

            if (year != 2000) {
                val month = ByteUtil.cbyte2Int(byteArray[1])
                val day = ByteUtil.cbyte2Int(byteArray[2])
                val hour = ByteUtil.cbyte2Int(byteArray[3])
                val minutes = ByteUtil.cbyte2Int(byteArray[4])
                val timeStringBuild = StringBuilder()
                timeStringBuild.append(year).append("-")
                timeStringBuild.append(String.format("%02d", month)).append("-")
                timeStringBuild.append(String.format("%02d", day)).append(" ")
                timeStringBuild.append(String.format("%02d", hour)).append(":")
                timeStringBuild.append(String.format("%02d", minutes))
                val temp = ByteUtil.cbyte2IntLow(byteArray, 5, 2)
                val humid = ByteUtil.cbyte2IntLow(byteArray, 7, 2)
                val tempAndHumidity =
                    TestBean(timeStringBuild.toString(), temp, humid)
                mutableList.add(tempAndHumidity)
            }
        }

        return mutableList
    }
}