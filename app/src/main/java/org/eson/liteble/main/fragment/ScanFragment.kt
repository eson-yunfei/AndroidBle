/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.eson.liteble.main.fragment

import android.bluetooth.BluetoothGatt
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shon.mvvm.base.ui.BaseBindingFragment
import kale.adapter.CommonRcvAdapter
import kale.adapter.item.AdapterItem
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.R
import org.eson.liteble.databinding.FragmentScanDeviceBinding
import org.eson.liteble.detail.DeviceActivity
import org.eson.liteble.detail.viewmodel.ConnectViewModel
import org.eson.liteble.detail.viewmodel.ConnectViewModel.ConnectView
import org.eson.liteble.main.MainActivity
import org.eson.liteble.main.ScannerViewModel
import org.eson.liteble.main.ScannerViewModel.ScannerView
import org.eson.liteble.main.adapter.ScanBLEItem
import java.util.*

/**
 * Created by xiaoyunfei on 2017/5/5.
 *
 *
 * update on 2020/12/06
 *
 *
 * 扫描设备界面
 */
class ScanFragment : BaseBindingFragment<FragmentScanDeviceBinding?>(), ScannerView, ConnectView {
    private val scannerViewModel: ScannerViewModel by viewModels()
    private var connectViewModel: ConnectViewModel? = null
    private lateinit var commonRcvAdapter: CommonRcvAdapter<ScanResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectViewModel = defaultViewModelProviderFactory.create(ConnectViewModel::class.java)
    }


    override fun onPause() {
        super.onPause()
        hideProgress()
    }
    override fun initViewState() {
        if (binding == null) {
            return
        }
        val layoutManager = LinearLayoutManager(activity)
        binding!!.listview.layoutManager = layoutManager
        commonRcvAdapter = object : CommonRcvAdapter<ScanResult>(ArrayList()) {
            override fun createItem(type: Any): AdapterItem<ScanResult> {
                return ScanBLEItem { bleDevice: ScanResult ->
                    val device = bleDevice.device
                    showProgress("Connecting...")
                    connectViewModel!!.connectDevice(device.address, device.name)
                }
            }
        }
        binding!!.listview.adapter = commonRcvAdapter
    }

    override fun onProcess(savedInstanceState: Bundle?) {
        scannerViewModel.attachView(this, this)
        connectViewModel!!.attachView(this, this)
    }

    override fun onFindDevices(scanResultList: MutableList<ScanResult>) {
        hideProgress()
        commonRcvAdapter!!.data = scanResultList
        commonRcvAdapter!!.notifyDataSetChanged()
    }

    override fun onFinishScanner() {
//        (requireActivity() as MainActivity).reSetMenu()
    }

    /**
     * 显示等待框
     *
     * @param msg msg
     */
    private fun showProgress(msg: String?) {
        binding?.loadingLayout?.visibility = View.VISIBLE
        binding?.msgText?.text = msg
    }

    private fun hideProgress() {
        binding?.loadingLayout?.visibility = View.GONE
    }

    fun stopScanner() {
        scannerViewModel.stopScanner()
        hideProgress()
    }

    fun startScanner() {
        showProgress(getString(R.string.text_scan_device))
        commonRcvAdapter!!.data = ArrayList()
        commonRcvAdapter!!.notifyDataSetChanged()
        scannerViewModel!!.startScanner()
    }

    override fun onConnected(deviceMac: String, gatt: BluetoothGatt) {}
    override fun onServerEnable(deviceMac: String, gatt: BluetoothGatt) {
        hideProgress()
        val intent = Intent(activity, DeviceActivity::class.java)
        intent.putExtra("connectBt", deviceMac)
        startActivity(intent)
    }

    override fun onDisconnected(deviceMac: String) {
        hideProgress()
    }

    override fun connectError(deviceMac: String, errorCode: Int) {
        hideProgress()
    }
}