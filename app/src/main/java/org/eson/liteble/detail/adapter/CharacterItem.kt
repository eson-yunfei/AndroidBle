//package org.eson.liteble.detail.adapter
//
//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.commitNow
//import androidx.fragment.app.transaction
//import com.shon.bluetooth.core.call.Listener
//import com.shon.bluetooth.core.call.NotifyCall
//import com.shon.bluetooth.core.call.ReadCall
//import com.shon.bluetooth.core.callback.NotifyCallback
//import com.shon.bluetooth.core.callback.ReadCallback
//import com.shon.bluetooth.util.ByteUtil
//import kale.adapter.item.AdapterItem
//import org.eson.liteble.R
//import org.eson.liteble.common.DeviceState
//import org.eson.liteble.common.util.BleUUIDUtil
//import org.eson.liteble.databinding.ItemUuidBinding
//import org.eson.liteble.detail.DeviceActivity
//import org.eson.liteble.detail.bean.BleDataBean
//import org.eson.liteble.detail.bean.CharacterBean
//import org.eson.liteble.detail.fragment.SendDataFragment
//import org.eson.log.LogUtils
//import java.util.*
//
//class CharacterItem : AdapterItem<CharacterBean> {
//    private lateinit var itemUuidBinding: ItemUuidBinding
//    override fun getLayoutResId(): Int = R.layout.item_uuid
//    private lateinit var characterBean: CharacterBean
//
//    private var sendDataFragment:SendDataFragment?= null
//    override fun bindViews(root: View) {
//        itemUuidBinding = ItemUuidBinding.bind(root)
//    }
//
//    override fun setViews() {
//
//
//        itemUuidBinding.notificationImage.setOnClickListener { startListenNotification() }
//
//        itemUuidBinding.readImage.setOnClickListener { readDeviceInfo() }
//
//        itemUuidBinding.sendImage.setOnClickListener {
//            val data = Bundle()
//            data.putString("serviceUUID",characterBean.serviceUUID)
//            data.putString("characterUUID",characterBean.characterUUID)
//            data.putString("address",characterBean.address)
//            if (sendDataFragment == null){
//
//                sendDataFragment = SendDataFragment()
//                sendDataFragment?.arguments = data
//                sendDataFragment?.show((itemUuidBinding.root.context as DeviceActivity).supportFragmentManager,"")
//            }
//
//        }
//    }
//
//    override fun handleData(characterBean: CharacterBean?, position: Int) {
//        characterBean ?: return
//        this.characterBean = characterBean
//        itemUuidBinding.notificationImage.visibility = View.GONE
//        itemUuidBinding.readImage.visibility = View.GONE
//        itemUuidBinding.sendImage.visibility = View.GONE
//
//        val characterUUID: String = characterBean.characterUUID!!
//        val characterName = BleUUIDUtil.getCharacterNameByUUID(UUID.fromString(characterUUID))
//        val hexString = BleUUIDUtil.getHexValue(UUID.fromString(characterUUID))
//
//        LogUtils.d("characterUUID = $characterUUID")
//        val resources = itemUuidBinding.root.context.resources
//        itemUuidBinding.characterNameText.text = characterName
//        itemUuidBinding.characterText.text = resources.getString(R.string.character_uuid,
//                hexString, characterUUID)
//
//        if (characterBean.write) {
//            itemUuidBinding.sendImage.visibility = View.VISIBLE
//        }
//        if (characterBean.read) {
//            itemUuidBinding.readImage.visibility = View.VISIBLE
//        }
//        if (characterBean.notify) {
//            itemUuidBinding.notificationImage.visibility = View.VISIBLE
//        }
//    }
//
//    private fun readDeviceInfo(){
//        ReadCall(characterBean.address)
//                .setServiceUUid(characterBean.serviceUUID)
//                .setCharacteristicUUID(characterBean.characterUUID)
//                .enqueue(object : ReadCallback() {
//                    override fun process(address: String, result: ByteArray): Boolean {
//                        if (address == characterBean.address) {
//
//                            val text = ByteUtil.getHexString(result)+ " (" + ByteUtil.byteToCharSequence(result) + ")"
//                            LogUtils.d("on Read info result = $text")
//                            itemUuidBinding.readResult.visibility = View.VISIBLE
//                            itemUuidBinding.readResult.text = text
//                            return true
//                        }
//                        return false
//                    }
//
//                    override fun onTimeout() {}
//                })
//    }
//
//    private fun startListenNotification(){
//
//        NotifyCall(characterBean.address)
//                .setServiceUUid(characterBean.serviceUUID)
//                .setCharacteristicUUID(characterBean.characterUUID)
//                .enqueue(object : NotifyCallback() {
//                    override fun getTargetSate(): Boolean {
//                        return true
//                    }
//
//                    override fun onChangeResult(result: Boolean) {
//                        super.onChangeResult(result)
//                        characterBean.listening = true
//                        startListener()
//                        itemUuidBinding.notificationImage.setImageResource(R.mipmap.ic_operation_start_notifications_pressed)
//                    }
//
//                    override fun onTimeout() {}
//                })
//    }
//
//    private fun startListener() {
//
//        Listener(characterBean.address)
//                .enqueue { address: String?, result: ByteArray? ->
//                    result?: false
//                    val bleDataBean = BleDataBean(address!!, characterBean.characterUUID!!,result!!)
//                    DeviceState.instance.addData(bleDataBean)
//                    true
//                }
//    }
//}