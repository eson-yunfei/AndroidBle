//package org.eson.liteble.detail.adapter
//
//import android.graphics.Color
//import android.text.Spannable
//import android.text.SpannableStringBuilder
//import android.text.style.ForegroundColorSpan
//import android.view.View
//import com.shon.bluetooth.util.ByteUtil
//import kale.adapter.item.AdapterItem
//import org.eson.liteble.R
//import org.eson.liteble.databinding.ItemDataBinding
//import org.eson.liteble.detail.bean.BleDataBean
//
//class DataItem : AdapterItem<BleDataBean> {
//    private lateinit var itemDataBinding: ItemDataBinding
//    override fun getLayoutResId(): Int  = R.layout.item_data
//
//    override fun bindViews(root: View) {
//        itemDataBinding = ItemDataBinding.bind(root)
//    }
//    override fun setViews() {}
//    override fun handleData(bleDataBean: BleDataBean, position: Int) {
//        bleDataBean?:return
//
//        val time = bleDataBean.time + " :"
//        val uuid = "\n${bleDataBean.uuid}\n"
//        val dataString: String = getDataString(bleDataBean.buffer)
//
//
//        val spannableStringBuilder = SpannableStringBuilder("$time$uuid${dataString.trimIndent()}")
//
//        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.BLACK),
//                0, time.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
//
//        itemDataBinding.dataText.text = spannableStringBuilder
//    }
//
//
//    private fun getDataString(buffer: ByteArray?): String {
//        var text = ByteUtil.getHexString(buffer)
////        if (!isUnknownCharacter) {
////            text = text + " (  " + ByteUtil.byteToCharSequence(buffer) + "  )"
////        }
//        return text
//    }
//}