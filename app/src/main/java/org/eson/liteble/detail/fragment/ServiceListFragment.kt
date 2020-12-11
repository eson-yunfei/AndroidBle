//package org.eson.liteble.detail.fragment
//
//import android.os.Bundle
//import androidx.lifecycle.ViewModelProviders
//import androidx.navigation.fragment.findNavController
//import com.shon.mvvm.base.ui.BaseBindingFragment
//import org.eson.liteble.common.DeviceState.DeviceLiveData
//import org.eson.liteble.R
//import org.eson.liteble.detail.bean.ServiceBean
//import org.eson.liteble.databinding.ActivityDetailBinding
//import org.eson.liteble.detail.DeviceActivity
//import org.eson.liteble.detail.viewmodel.ConnectViewModel
//import org.eson.liteble.detail.viewmodel.DeviceControlViewModel
//import org.eson.liteble.detail.adapter.DeviceDetailAdapter
//import org.eson.liteble.detail.task.ReadRssiTask
//
///**
// * Auth : xiao.yunfei
// * Date : 2020/6/20 14:12
// * Package name : org.eson.liteble.activity.fragment
// * Des : 支持服务列表界面
// */
//class ServiceListFragment : BaseBindingFragment<ActivityDetailBinding?>() {
//    private var controlViewModel: DeviceControlViewModel? = null
//    private lateinit var mDeviceDetailAdapter: DeviceDetailAdapter
//    private var connectViewModel: ConnectViewModel? = null
//    private var deviceMac: String? = null
//    private var isReadStart = false
//    override fun initViewState() {
//
//        mDeviceDetailAdapter = DeviceDetailAdapter(activity, mutableListOf())
//        mDeviceDetailAdapter.setOnItemClickListener { serviceBean: ServiceBean?, position: Int ->
//            val bundle = Bundle()
//            bundle.putParcelable("serviceBean", serviceBean)
//            bundle.putInt("position", position)
//            bundle.putString("address", deviceMac)
//            findNavController().navigate(R.id.action_serviceListFragment_to_serviceInfoFragment, bundle)
//        }
//        binding?.detailList?.adapter = mDeviceDetailAdapter
//    }
//
//    override fun initViewListener() {
////        binding?.disconnect?.setOnClickListener {
////            connectViewModel?.disConnectDevice(deviceMac)
////        }
//        binding?.readRssiBtn?.setOnClickListener {
//            if (isReadStart) {
//
//                isReadStart = false;
//                binding?.readRssiBtn?.text = "开始读取信号值";
//                stopReadTimer();
//
//            } else {
//                isReadStart = true;
//                binding?.readRssiBtn?.text = "停止读取信号值";
//                startReadTimer();
//            }
//        }
//    }
//
//
//    override fun onProcess(savedInstanceState: Bundle?) {
//        super.onProcess(savedInstanceState)
//        connectViewModel = defaultViewModelProviderFactory.create(ConnectViewModel::class.java)
//        controlViewModel = ViewModelProviders.of(requireActivity()).get(DeviceControlViewModel::class.java)
//        controlViewModel!!.mutableLiveData.observe(this, { serviceBeanList: List<ServiceBean> -> setAdapter(serviceBeanList) })
//    }
//    //    @Override
//    //    protected void onProcess() {
//    //        super.onProcess();
//    //
//    //        Map<String, NavArgument> map = NavHostFragment.findNavController(this).getGraph().getArguments();
//    //        NavArgument navArgument = map.get("connectBt");
//    //        if (navArgument == null) {
//    //            return;
//    //        }
//    //    }
//    /**
//     * @param serviceBeanList serviceBeanList
//     */
//    private fun setAdapter(serviceBeanList: List<ServiceBean>) {
//
//        mDeviceDetailAdapter.setDataList(serviceBeanList)
//        mDeviceDetailAdapter.notifyDataSetChanged()
//        controlViewModel!!.observerDeviceLiveData(this, { deviceLiveData: DeviceLiveData? ->
//            deviceLiveData ?: return@observerDeviceLiveData
//
//            (requireActivity() as DeviceActivity).supportActionBar?.title = deviceLiveData.deviceName
////            binding?.name?.text = deviceLiveData.deviceName
//            deviceMac = deviceLiveData.deviceMac
//        })
//    }
//
//    private var readRssiTask: ReadRssiTask? = null
//    private fun startReadTimer() {
//        if (readRssiTask == null) {
//            readRssiTask = ReadRssiTask(deviceMac!!, "")
//        }
//        readRssiTask?.startTask()
//    }
//
//    private fun stopReadTimer() {
//        if (readRssiTask == null) {
//            return
//        }
//        readRssiTask?.stopTask()
//
//    }
//
//}