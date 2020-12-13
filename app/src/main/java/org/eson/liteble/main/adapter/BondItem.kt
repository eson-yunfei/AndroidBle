package org.eson.liteble.main.adapter

import android.annotation.SuppressLint
import android.view.View
import kale.adapter.item.AdapterItem
import org.eson.liteble.R
import org.eson.liteble.common.DeviceState
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
class BondItem() : AdapterItem<DeviceState.DeviceLiveData> {

    private lateinit var binding: ItemScanDeviceBinding
    override fun getLayoutResId(): Int = R.layout.item_scan_device
    override fun bindViews(view: View) {
        binding = ItemScanDeviceBinding.bind(view)
    }

    override fun setViews() {}

    @SuppressLint("SetTextI18n")
    override fun handleData(device: DeviceState.DeviceLiveData, position: Int) {
        val name = device.deviceName
        val mac = device.deviceMac

        binding.deviceName.text = name
        binding.deviceMac.text = mac
        binding.deviceRssi.visibility = View.GONE
        binding.scanRet.visibility = View.GONE

    }
}