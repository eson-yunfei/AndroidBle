package org.eson.liteble.detail.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.eson.liteble.common.util.BleUUIDUtil
import kale.adapter.CommonRcvAdapter
import kale.adapter.item.AdapterItem
import org.eson.liteble.R
import org.eson.liteble.databinding.ItemUuidGroupBinding
import org.eson.liteble.detail.bean.CharacterBean
import org.eson.liteble.detail.bean.ServiceBean
import java.util.*

class ServiceUuidItem : AdapterItem<ServiceBean> {

    private lateinit var itemUuidGroupBinding: ItemUuidGroupBinding
    override fun getLayoutResId(): Int = R.layout.item_uuid_group

    override fun bindViews(root: View) {
        itemUuidGroupBinding = ItemUuidGroupBinding.bind(root)

    }

    override fun setViews() {
    }

    override fun handleData(serviceBean: ServiceBean, position: Int) {
        val uuid = serviceBean.serviceUUID
        uuid?:return
        val serviceName = BleUUIDUtil.getServiceNameByUUID(UUID.fromString(uuid))
        val shortUUID = BleUUIDUtil.getHexValue(UUID.fromString(uuid))
        val serviceType: String = serviceBean.serviceType

        val context = itemUuidGroupBinding.root.context
        val serviceUUIDString: String = context.getString(R.string.service_uuid_info,
                shortUUID, serviceType, serviceBean.serviceUUID)


        itemUuidGroupBinding.serverNameText.text = serviceName
        itemUuidGroupBinding.serverUuidText.text = serviceUUIDString

        val characterBeanList: List<CharacterBean> = serviceBean.uuidBeen

        val uuidAdapter: CommonRcvAdapter<CharacterBean> =object :CommonRcvAdapter<CharacterBean>(characterBeanList){
            override fun createItem(type: Any?): AdapterItem<CharacterBean> {
                return CharacterItem()
            }

        }
        itemUuidGroupBinding.characterList.layoutManager = LinearLayoutManager(context)
//        val uuidAdapter = UUIDAdapter(context, characterBeanList)
        itemUuidGroupBinding.characterList.adapter = uuidAdapter
//            setItemClickListener(viewHolder.mListView, serviceBean)

    }

}