package org.eson.liteble.detail

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.shon.mvvm.base.ui.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kale.adapter.CommonRcvAdapter
import kale.adapter.item.AdapterItem
import org.eson.liteble.R
import org.eson.liteble.common.DeviceState
import org.eson.liteble.databinding.ActivityDetailBinding
import org.eson.liteble.detail.adapter.ServiceUuidItem
import org.eson.liteble.detail.bean.ServiceBean
import org.eson.liteble.detail.fragment.DeviceLogDialogFragment
import org.eson.liteble.detail.viewmodel.ConnectViewModel
import org.eson.liteble.detail.viewmodel.DeviceControlViewModel
import org.eson.toast.ToastUtils

@AndroidEntryPoint
class DeviceActivity : BaseBindingActivity<ActivityDetailBinding?>(), ConnectViewModel.ConnectView {

    private val deviceControlViewModel: DeviceControlViewModel by viewModels()
    private val connectViewModel: ConnectViewModel by viewModels()
    private lateinit var mDeviceDetailAdapter: CommonRcvAdapter<ServiceBean>
    private var isReadStart = false
    private var menuDeviceState: MenuItem? = null
    private var menuRefresh: MenuItem? = null
    private var logDialogFragment: DeviceLogDialogFragment? = null
    private var connectState = true

    override fun initViewState() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.detailList?.layoutManager = LinearLayoutManager(this)

        mDeviceDetailAdapter = object : CommonRcvAdapter<ServiceBean>(mutableListOf()) {
            override fun createItem(type: Any?): AdapterItem<ServiceBean> {
                return ServiceUuidItem()
            }
        }
        binding?.detailList?.adapter = mDeviceDetailAdapter
    }

    override fun initViewListener() {

        binding?.readRssiBtn?.setOnClickListener {
            if (isReadStart) {

                isReadStart = false
                binding?.readRssiBtn?.text = "开始读取信号值";
                connectViewModel.stopReadTimer()

            } else {
                isReadStart = true
                binding?.readRssiBtn?.text = "停止读取信号值";
                connectViewModel.startReadTimer()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menuDeviceState = menu.findItem(R.id.device_state)
        menuRefresh = menu.findItem(R.id.menu_refresh)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.device_state) {
            if (connectState) {
                connectViewModel.disConnectDevice()
            } else {
                connectViewModel.connectDevice()
                setActionMenuState(0)
            }
        }
        if (item.itemId == R.id.device_log) {
            showLogDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogDialog() {

        if (logDialogFragment == null) {
            logDialogFragment = DeviceLogDialogFragment()
        }
        if (logDialogFragment?.isHidden == false) {
            logDialogFragment?.show(supportFragmentManager, "")
        }
    }

    private fun setActionMenuState(connectState:Int) {
        when(connectState){
            DeviceState.DeviceLiveData.STATE_SERVER_ENABLE->{
                menuRefresh?.isVisible = false
                menuDeviceState?.isVisible = true
                menuRefresh?.actionView = null
                menuDeviceState?.setIcon(R.drawable.ic_bluetooth_connected)
            }
            DeviceState.DeviceLiveData.STATE_DIS_CONNECTED->{
                menuRefresh?.isVisible = false
                menuDeviceState?.isVisible = true
                menuRefresh?.actionView = null
                menuDeviceState?.setIcon(R.mipmap.ic_action_disconnect)
            }
            DeviceState.DeviceLiveData.STATE_CONNECT_ERROR->{
                menuRefresh?.isVisible = false
                menuDeviceState?.isVisible = true
                menuRefresh?.actionView = null
                menuDeviceState?.setIcon(R.mipmap.ic_action_disconnect)
            }
            else->{
                menuRefresh?.isVisible = true
                menuDeviceState?.isVisible = false
                menuRefresh?.setActionView(R.layout.action_bar_progress)
            }
        }
    }

    override fun onProcess(bundle: Bundle?) {
        val device: BluetoothDevice? = intent.getParcelableExtra("connectBt")
        device ?: return
        supportActionBar?.title = device.name
        connectViewModel.attachView(this, this)
        connectViewModel.setCurrentDevice(device)

        deviceControlViewModel.mutableLiveData.observe(this, { serviceBeanList: List<ServiceBean> -> setAdapter(serviceBeanList) })
    }

    override fun onResume() {
        super.onResume()
        setActionMenuState(0)
        connectViewModel.connectDevice()
    }

    override fun onStop() {
        super.onStop()
        connectViewModel.disConnectDevice()
    }
    private fun setAdapter(serviceBeanList: List<ServiceBean>) {

        mDeviceDetailAdapter.data = serviceBeanList
        mDeviceDetailAdapter.notifyDataSetChanged()

    }

    override fun onConnected(deviceMac: String?, gatt: BluetoothGatt?) {
        connectState = true
        ToastUtils.showShort(this, "连接成功")
    }

    override fun onServerEnable(deviceMac: String?, gatt: BluetoothGatt?) {
        menuDeviceState?.setIcon(R.drawable.ic_bluetooth_connected)
        gatt ?: return
        deviceControlViewModel.loadGattServer(gatt)
        setActionMenuState(DeviceState.DeviceLiveData.STATE_SERVER_ENABLE)
    }

    override fun onDisconnected(deviceMac: String?) {
        connectState = false

        menuDeviceState?.setIcon(R.mipmap.ic_action_disconnect)
        mDeviceDetailAdapter.data = arrayListOf()
        mDeviceDetailAdapter.notifyDataSetChanged()
        setActionMenuState(0)
        Handler(Looper.getMainLooper())
                .postDelayed(1_500){
                    ToastUtils.showShort(this, "断开连接")
                    setActionMenuState(DeviceState.DeviceLiveData.STATE_DIS_CONNECTED)
                }
    }

    override fun connectError(deviceMac: String?, errorCode: Int) {
        connectState = false
        ToastUtils.showShort(this, "断开连接")
        menuDeviceState?.setIcon(R.mipmap.ic_action_disconnect)
        mDeviceDetailAdapter.data = arrayListOf()
        mDeviceDetailAdapter.notifyDataSetChanged()
        setActionMenuState(DeviceState.DeviceLiveData.STATE_CONNECT_ERROR)
    }

}
