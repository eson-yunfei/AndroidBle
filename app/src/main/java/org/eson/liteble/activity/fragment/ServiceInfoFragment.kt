package org.eson.liteble.activity.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.shon.bluetooth.core.call.Listener
import com.shon.bluetooth.core.call.NotifyCall
import com.shon.bluetooth.core.call.ReadCall
import com.shon.bluetooth.core.callback.NotifyCallback
import com.shon.bluetooth.core.callback.ReadCallback
import com.shon.bluetooth.util.BleUUIDUtil
import com.shon.mvvm.base.ui.BaseBindingFragment
import org.eson.liteble.R
import org.eson.liteble.activity.adapter.BleDataAdapter
import org.eson.liteble.activity.bean.BleDataBean
import org.eson.liteble.activity.bean.CharacterBean
import org.eson.liteble.activity.bean.ServiceBean
import org.eson.liteble.databinding.ActivityCharacteristicBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:15
 * Package name : org.eson.liteble.activity.fragment
 * Des :
 */
class ServiceInfoFragment : BaseBindingFragment<ActivityCharacteristicBinding?>(), View.OnClickListener {
    private var characterBean: CharacterBean? = null
    private var serviceUUID: String? = null
    private var characterUUID: String? = null
    private var characterName = ""
    private var connectMac: String? = null
    private var isListenerNotice = false
    private val descriptors: MutableList<String> = ArrayList()
    private val bleDataList: MutableList<BleDataBean> = ArrayList()
    private var adapter: BleDataAdapter? = null
    override fun initViewListener() {
        binding!!.readBtn.setOnClickListener(this)
        binding!!.writeBtn.setOnClickListener(this)
        binding!!.notifyBtn.setOnClickListener(this)
    }

    override fun setArguments(bundle: Bundle?) {
        super.setArguments(bundle)
        if (bundle == null) {
            return
        }
        val serviceBean: ServiceBean = bundle!!.getParcelable("serviceBean")!!
        val position = bundle.getInt("position")
        connectMac = bundle.getString("address")
        if (serviceBean == null) {
            return
        }
        characterBean = serviceBean.uuidBeen[position]
        serviceUUID = characterBean?.serviceUUID
        characterUUID = characterBean?.characterUUID
        characterName = BleUUIDUtil.getCharacterNameByUUID(UUID.fromString(characterUUID))
        isListenerNotice = characterBean?.isListening!!
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    fun startListener() {
        Listener(connectMac)
                .enqueue { address: String?, result: ByteArray? ->
                    changeBleData("", result, address)
                    true
                }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.writeBtn -> {
                val bundle = Bundle()
                bundle.putString("serviceUUID", serviceUUID)
                bundle.putString("characterUUID", characterUUID)
                bundle.putString("address", connectMac)
                Navigation.findNavController(binding!!.root)
                        .navigate(R.id.action_serviceInfoFragment_to_sendDataFragment, bundle)
            }
            R.id.readBtn -> readCharacter()
            R.id.notifyBtn -> enableNotice()
            else -> {
            }
        }
    }

    private fun readCharacter() {
        ReadCall(connectMac)
                .setServiceUUid(serviceUUID)
                .setCharacteristicUUID(characterUUID)
                .enqueue(object : ReadCallback() {
                    override fun process(address: String, result: ByteArray): Boolean {
                        changeBleData(characterUUID, result, address)
                        return true
                    }

                    override fun onTimeout() {}
                })
    }

    /**
     * 启动通知服务
     */
    private fun enableNotice() {
        isListenerNotice = !isListenerNotice
        val text = if (isListenerNotice) "取消监听" else "开始监听"
        binding!!.notifyBtn.text = text
        characterBean!!.isListening = isListenerNotice
        NotifyCall(connectMac)
                .setServiceUUid(serviceUUID)
                .setCharacteristicUUID(characterUUID)
                .enqueue(object : NotifyCallback() {
                    override fun getTargetSate(): Boolean {
                        return true
                    }

                    override fun onChangeResult(result: Boolean) {
                        super.onChangeResult(result)
                        startListener()
                    }

                    override fun onTimeout() {}
                })
    }

     fun changeBleData(uuid: String?, buffer: ByteArray?, deviceAddress: String?) {
        val bean = BleDataBean(deviceAddress, null /* UUID.fromString(uuid)*/, buffer)
        bean.time = currentTime
        bleDataList.add(0, bean)
        if (adapter == null) {
            adapter = BleDataAdapter(activity, bleDataList, characterName)
            binding!!.dataListView.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss ,  SSS", Locale.getDefault())
    private val currentTime: String
        private get() = simpleDateFormat.format(Date(System.currentTimeMillis()))

    /**
     *
     */
    private fun setData() {
        binding!!.uuidText.text = characterUUID
        var name = ""
        if (characterBean!!.isRead) {
            name += "read "
            binding!!.readBtn.visibility = View.VISIBLE
        }
        if (characterBean!!.isWrite) {
            name += "write "
            binding!!.writeBtn.visibility = View.VISIBLE
        }
        if (characterBean!!.isNotify) {
            name += "notify "
            binding!!.notifyBtn.visibility = View.VISIBLE
        }
        binding!!.propertiesText.text = name
        val text = if (isListenerNotice) "取消监听" else "开始监听"
        binding!!.notifyBtn.text = text
        val descriptorList = characterBean!!.descriptorBeen
        if (descriptorList == null || descriptorList.size == 0) {
            binding!!.descriptorLayout.visibility = View.GONE
            return
        }
        for (descriptorBean in descriptorList) {
            val uuid = descriptorBean.uuid
            descriptors.add(uuid)
        }
        val arrayAdapter = ArrayAdapter(activity!!,
                android.R.layout.simple_list_item_1, android.R.id.text1, descriptors)
        binding!!.descListView.adapter = arrayAdapter
    }
}