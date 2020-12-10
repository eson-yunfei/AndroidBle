package org.eson.liteble.setting

import android.icu.number.IntegerWidth
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.CompoundButton
import com.shon.mvvm.base.ui.BaseBindingActivity
import org.eson.liteble.R
import org.eson.liteble.common.share.ConfigPreferences.Companion.connectMore
import org.eson.liteble.common.share.ConfigPreferences.Companion.filterNoName
import org.eson.liteble.common.share.ConfigPreferences.Companion.maxConnect
import org.eson.liteble.common.share.ConfigPreferences.Companion.scannerTime
import org.eson.liteble.databinding.ActivitySettingBinding
import org.eson.log.LogUtils

/**
 * @name AndroidBle
 * @class name：org.eson.liteble.activity
 * @class describe
 * @anthor xujianbo E-mail: xuarbo@qq.com
 * @time 2017/2/22 18:21
 * @change 新增过滤无名称设备
 * @chang 2020-06-19
 * @class describe
 */
class SettingActivity : BaseBindingActivity<ActivitySettingBinding?>() {
    private lateinit var intArray: List<Int>
    private lateinit var maxConnectArray: List<Int>
    override fun initViewState() {
        val supportActionBar = supportActionBar ?: return
        supportActionBar.setHomeButtonEnabled(true)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun initViewListener() {
        super.initViewListener()
        binding!!.scanTimeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val scanTime = intArray[position]
                scannerTime = scanTime
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding!!.maxNumber.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val maxLink = maxConnectArray[position]
                maxConnect = maxLink
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding!!.switchBtn.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            connectMore = isChecked
            val visibility1 = if (isChecked) View.VISIBLE else View.GONE
            binding!!.maxLinkGroup.visibility = visibility1
        }
        binding!!.filterNoName.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> filterNoName = isChecked }
    }

    override fun onProcess(bundle: Bundle?) {
        intArray = resources.getStringArray(R.array.scanner_time).map {
            val value = Integer.parseInt(it)
            value
        }
        maxConnectArray = resources.getStringArray(R.array.max_connect_size).map {
            val value = Integer.parseInt(it)
            value
        }
        var scannerIndex = 0
        for (i in intArray.indices) {
            if (scannerTime == intArray[i]) {
                scannerIndex = i
            }
        }
        binding!!.scanTimeSpinner.setSelection(scannerIndex)
        val permit = connectMore
        val visibility = if (permit) View.VISIBLE else View.GONE
        binding!!.maxLinkGroup.visibility = visibility
        binding!!.switchBtn.isChecked = permit
        var maxLinkIndex = 0
        for (i in maxConnectArray.indices) {
            if (maxConnect == maxConnectArray[i]) {
                maxLinkIndex = i
            }
        }
        binding!!.maxNumber.setSelection(maxLinkIndex)
        binding!!.filterNoName.isChecked = filterNoName
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}