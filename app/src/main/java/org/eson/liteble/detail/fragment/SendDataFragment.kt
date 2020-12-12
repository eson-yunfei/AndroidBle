package org.eson.liteble.detail.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.shon.bluetooth.core.call.WriteCall
import com.shon.bluetooth.core.callback.WriteCallback
import com.shon.bluetooth.util.ByteUtil
import org.eson.liteble.databinding.ActivitySendDataBinding
import org.eson.test.band.BleCmd
import org.eson.toast.ToastUtils

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:16
 * Package name : org.eson.liteble.activity.fragment
 * Des :
 */
class SendDataFragment : DialogFragment() {
    private lateinit var binding: ActivitySendDataBinding
    private var serviceUUID: String? = null
    private var characterUUID: String? = null
    private var connectMac: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivitySendDataBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewListener()
    }
    private fun initViewListener() {
        binding.sendBtn.setOnClickListener { v: View? -> sendData() }
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
        val data :String?= binding.editText.text.toString()

        if (TextUtils.isEmpty(data)){
            return
        }
        data?:return
        if (data.length % 2 != 0) {
            ToastUtils.showShort(requireActivity(),"请检查数据长度")
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