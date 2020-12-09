package org.eson.liteble.activity.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.shon.bluetooth.core.call.WriteCall
import com.shon.bluetooth.core.callback.WriteCallback
import com.shon.bluetooth.util.ByteUtil
import com.shon.mvvm.base.ui.BaseBindingFragment
import org.eson.liteble.databinding.ActivitySendDataBinding

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:16
 * Package name : org.eson.liteble.activity.fragment
 * Des :
 */
class SendDataFragment : BaseBindingFragment<ActivitySendDataBinding?>() {
    private var serviceUUID: String? = null
    private var characterUUID: String? = null
    private var connectMac: String? = null
    override fun initViewListener() {
        binding!!.sendBtn.setOnClickListener { v: View? -> sendData() }
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        if (args == null) {
            return
        }
        serviceUUID = args.getString("serviceUUID")
        characterUUID = args.getString("characterUUID")
        connectMac = args.getString("address")
    }

    private fun sendData() {
        val data = binding!!.editText.text.toString()
        if (TextUtils.isEmpty(data)) {
            return
        }
        if (data.length % 2 != 0) {
            return
        }
        val buffer = ByteUtil.hexStringToByte(data)
        WriteCall(connectMac)
                .setServiceUUid(serviceUUID)
                .setCharacteristicUUID(characterUUID)
                .enqueue(object : WriteCallback(connectMac) {
                    override fun getSendData(): ByteArray {
                        return buffer
                    }

                    override fun process(address: String, result: ByteArray): Boolean {
                        return false
                    }

                    override fun removeOnWriteSuccess(): Boolean {
                        return true
                    }

                    override fun onTimeout() {}
                })
    }
}