package org.eson.liteble.main.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.util.isEmpty
import kale.adapter.item.AdapterItem
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.R
import org.eson.liteble.common.util.ByteUtil
import org.eson.liteble.databinding.ItemScanDeviceBinding

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity.adapter
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 15:40
 * @change  update on 2020/12/13 by xiao yun fei
 * @chang time
 * @class describe
 */
class ScanBLEItem(private val mOnClickListener: (ScanResult)->Unit) : AdapterItem<ScanResult> {

    private lateinit var binding: ItemScanDeviceBinding
    override fun getLayoutResId(): Int = R.layout.item_scan_device
    override fun bindViews(view: View) {
        binding = ItemScanDeviceBinding.bind(view)
    }

    override fun setViews() {}

    @SuppressLint("SetTextI18n")
    override fun handleData(device: ScanResult, position: Int) {
        val name = device.device.name
        val mac = device.device.address
        binding.deviceRssi.text = "${device.rssi}"
        binding.deviceName.text = name
        binding.deviceMac.text = mac
        binding.scanRet.visibility = View.GONE
        binding.root.setOnClickListener { mOnClickListener.invoke(device) }


//        LogUtils.d("device.isLegacy() = "+ device.isLegacy());
//        LogUtils.d("device.isConnectable() = "+ device.isConnectable());
//        TimeTest.test(device.timestampNanos)
        val scanRecord = device.scanRecord ?: return
        val array = scanRecord.manufacturerSpecificData
        array?:return
        if (array.isEmpty()){
            return
        }
        val builder = StringBuilder()

        for (i in 0 until array.size()) {
            val key = array.keyAt(i)
            val b = array[key]
            builder.append(ByteUtil.getFormatHexString(b))
//            val s = com.shon.bluetooth.util.ByteUtil.byteToCharSequenceUTF(b)
//            LogUtils.d("s == $s")
            if (i != array.size() - 1) {
                builder.append("\n")
            }
        }
        binding.scanRet.visibility = View.VISIBLE
        binding.scanRet.text = builder.toString()
    }
}