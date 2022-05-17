package org.eson.liteble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.eson.liteble.main.composable.HomeScreen

/**
 * 主界面
 * 包含 ：
 * 1、蓝牙设备搜索
 * 2、已连接的设备管理
 * 3、菜单功能
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "Home") {
                composable("Home") { HomeScreen() }
                composable("detail/{deviceAddress}") {}
            }

        }
    }
//    private lateinit var mScanFragment: ScanFragment
//    private lateinit var mDevicesFragment: BondedFragment
//    private var menuRefresh: MenuItem? = null
//    private var menuScan: MenuItem? = null
//
//    private var lastSelectTabIndex = 0;
//    override fun initViewState() {
////        val mActionBar = supportActionBar
////        if (mActionBar != null) {
////            mActionBar.elevation = 0f
////        }
//        mScanFragment = ScanFragment()
//        mDevicesFragment = BondedFragment()
//
//
//        supportFragmentManager.commitNow {
//            add(R.id.containerLayout, mScanFragment)
//        }
//    }
//
//    override fun initViewListener() {
//        super.initViewListener()
//
//        binding?.tabLayout?.addOnTabSelectedListener(onTabSelectedListener)
//    }
//
//    override fun onProcess(bundle: Bundle?) {
//        checkBLEState()
//    }
//
//    private fun checkBLEState() {
//        val request = PermissionRequest(this)
//        request.requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_WIFI_STATE,
//                Manifest.permission.BLUETOOTH_ADMIN)
//        request.setOnRequestCallBack { granted: Boolean ->
//            if (granted) {
//                ToastUtils.showShort(this@MainActivity, "获取蓝牙权限成功")
//            }
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        reSetMenu()
//    }
//
//    /**
//     *
//     */
//    private val onTabSelectedListener: OnTabSelectedListener = object : OnTabSelectedListener {
//        override fun onTabSelected(tab: TabLayout.Tab) {
//            onTabChanged(tab)
//        }
//
//        override fun onTabUnselected(tab: TabLayout.Tab) {}
//        override fun onTabReselected(tab: TabLayout.Tab) {}
//    }
//
//    /**
//     * @param tab tab
//     */
//    private fun onTabChanged(tab: TabLayout.Tab) {
//        val currentIndex = tab.position
//        if (currentIndex == lastSelectTabIndex){
//            return
//        }
//        lastSelectTabIndex = currentIndex
//        reSetMenu()
//        when (lastSelectTabIndex) {
//            0 -> supportFragmentManager.commitNow {
//                replace(R.id.containerLayout, mScanFragment)
//            }
//            1 -> {
//                supportFragmentManager.commitNow {
//                    replace(R.id.containerLayout, mDevicesFragment)
//                }
//                menuScan!!.isVisible = false
//            }
//        }
//    }
//
//    fun reSetMenu() {
//        if (menuRefresh == null) {
//            return
//        }
//        menuRefresh!!.isVisible = false
//        menuRefresh?.actionView = null
//        menuScan!!.title = "Scan"
//        menuScan!!.isVisible = true
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        menuRefresh = menu.findItem(R.id.menu_refresh)
//        menuScan = menu.findItem(R.id.menu_scan)
//        reSetMenu()
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.action_settings) {
//            startToSetting()
//            return true
//        }
//        if (id == R.id.action_about) {
//            startToAbout()
//            return true
//        }
//        if (id == R.id.menu_scan) {
//            if (menuRefresh!!.isVisible) {
//                mScanFragment!!.stopScanner()
//                reSetMenu()
//            } else {
//                mScanFragment!!.startScanner()
//                menuRefresh!!.isVisible = true
//                menuRefresh!!.setActionView(R.layout.action_bar_progress)
//                menuScan!!.title = "Scanning"
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun startToAbout() {
//        val intent = Intent(this@MainActivity, AboutActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun startToSetting() {
//        val intent = Intent(this@MainActivity, SettingActivity::class.java)
//        startActivity(intent)
//    }
}