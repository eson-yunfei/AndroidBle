package org.eson.liteble.detail.adapter

import android.view.View
import org.eson.liteble.common.util.BleUUIDUtil
import kale.adapter.item.AdapterItem
import org.eson.liteble.R
import org.eson.liteble.databinding.ItemUuidBinding
import org.eson.liteble.detail.bean.CharacterBean
import java.util.*

class CharacterItem : AdapterItem<CharacterBean> {
    private lateinit var itemUuidBinding: ItemUuidBinding
    override fun getLayoutResId(): Int = R.layout.item_uuid

    override fun bindViews(root: View) {
        itemUuidBinding = ItemUuidBinding.bind(root)
    }

    override fun setViews() {

    }

    override fun handleData(characterBean: CharacterBean?, position: Int) {
        characterBean ?: return
        val characterUUID: String = characterBean.characterUUID
        val characterName = BleUUIDUtil.getCharacterNameByUUID(UUID.fromString(characterUUID))
        val hexString = BleUUIDUtil.getHexValue(UUID.fromString(characterUUID))

        val resources = itemUuidBinding.root.context.resources
        itemUuidBinding.characterNameText.text = characterName
        itemUuidBinding.characterText.text = resources.getString(R.string.character_uuid,
                hexString, characterUUID)
        //getDesc(characterBean)
    }

    private fun getDesc(characterBean: CharacterBean): String {
        var desc = ""
        if (characterBean.isWrite) {
            desc += "write  "
        }
        if (characterBean.isRead) {
            desc += "read  "
        }
        if (characterBean.isNotify) {
            desc += "notify  "
        }
        return desc
    }
}