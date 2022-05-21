//package org.eson.liteble.detail
//
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import androidx.activity.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.shon.mvvm.base.ui.BaseBindingActivity
//import kale.adapter.CommonRcvAdapter
//import kale.adapter.item.AdapterItem
//import org.eson.liteble.R
//import org.eson.liteble.common.DeviceState
//import org.eson.liteble.databinding.ActivityDetailBinding
//import org.eson.liteble.detail.adapter.ServiceUuidItem
//import org.eson.liteble.detail.bean.ServiceBean
//import org.eson.liteble.detail.fragment.DeviceLogDialogFragment
//import org.eson.liteble.detail.task.ReadRssiTask
//import org.eson.liteble.detail.viewmodel.ConnectViewModel
//import org.eson.liteble.detail.viewmodel.DeviceControlViewModel
//
//class DeviceActivity : BaseBindingActivity<ActivityDetailBinding?>() {
//
//    private val deviceControlViewModel: DeviceControlViewModel by viewModels()
//
//    private var connectDeviceAddress: String? = null
//
//    private lateinit var mDeviceDetailAdapter: CommonRcvAdapter<ServiceBean>
//    private var connectViewModel: ConnectViewModel? = null
//    private var deviceMac: String? = null
//    private var isReadStart = false
//    private var menuDeviceState: MenuItem? = null
//    private var logDialogFragment:DeviceLogDialogFragment? = null
//
//    override fun initViewState() {
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        binding?.detailList?.layoutManager = LinearLayoutManager(this)
//
//        mDeviceDetailAdapter = object : CommonRcvAdapter<ServiceBean>(mutableListOf()) {
//            override fun createItem(type: Any?): AdapterItem<ServiceBean> {
//                return ServiceUuidItem()
//            }
//        }
//
//        binding?.detailList?.adapter = mDeviceDetailAdapter
//
//    }
//
//
//    override fun initViewListener() {
//
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
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_detail, menu)
//        menuDeviceState = menu.findItem(R.id.device_state)
//        return true
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            finish()
//            return true
//        }
//        if (item.itemId == R.id.device_state) {
//            connectViewModel?.disConnectDevice(deviceMac)
//        }
//        if (item.itemId == R.id.device_log){
//            showLogDialog()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun showLogDialog() {
//
//        if (logDialogFragment == null) {
//            logDialogFragment = DeviceLogDialogFragment()
//        }
//        if (logDialogFragment?.isHidden == false) {
//            logDialogFragment?.show(supportFragmentManager, "")
//        }
//
//    }
//
//    override fun onProcess(bundle: Bundle?) {
//        connectDeviceAddress = intent.getStringExtra("connectBt")
//        deviceControlViewModel.setConnectDevice(connectDeviceAddress)
//
//        connectViewModel = defaultViewModelProviderFactory.create(ConnectViewModel::class.java)
//        deviceControlViewModel.mutableLiveData.observe(this, { serviceBeanList: List<ServiceBean> -> setAdapter(serviceBeanList) })
//
//    }
//
//    private fun setAdapter(serviceBeanList: List<ServiceBean>) {
//
//        mDeviceDetailAdapter.data = serviceBeanList
//        mDeviceDetailAdapter.notifyDataSetChanged()
//        deviceControlViewModel!!.observerDeviceLiveData(this, { deviceLiveData: DeviceState.DeviceLiveData? ->
//            deviceLiveData ?: return@observerDeviceLiveData
//
//            supportActionBar?.title = deviceLiveData.deviceName
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
