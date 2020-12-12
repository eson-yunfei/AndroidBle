package org.eson.liteble.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kale.adapter.CommonRcvAdapter
import kale.adapter.item.AdapterItem
import org.eson.liteble.common.DeviceState
import org.eson.liteble.databinding.FragmentDeviceLogBinding
import org.eson.liteble.detail.adapter.DataItem
import org.eson.liteble.detail.bean.BleDataBean

class DeviceLogDialogFragment : DialogFragment(){
    private lateinit var binding: FragmentDeviceLogBinding
    private lateinit var dataAdapter:CommonRcvAdapter<BleDataBean>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeviceLogBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataAdapter = object :CommonRcvAdapter<BleDataBean>(listOf()){
            override fun createItem(type: Any?): AdapterItem<BleDataBean> {
                return DataItem()
            }
        }


        binding.dataList.adapter = dataAdapter

        DeviceState.instance.dataListLiveData.observe(this){
            dataAdapter.data = it
            dataAdapter.notifyDataSetChanged()
        }
    }
}
