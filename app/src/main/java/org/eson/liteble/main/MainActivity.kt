package org.eson.liteble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.shon.bluetooth.util.BleLog
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.main.composable.DetailScreen
import org.eson.liteble.main.composable.HomeScreen

/**
 * 主界面
 * 包含 ：
 * 1、蓝牙设备搜索
 * 2、已连接的设备管理
 * 3、菜单功能
 */
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scanViewModel: ScanViewModel = viewModel()
            val permissionState =
                rememberMultiplePermissionsState(permissions = getPermissionList())
            val navController = rememberNavController()
            val rememberList = remember {
                mutableStateListOf<ScanResult>()
            }
            scanViewModel.scanResult.observe(this) {
                BleLog.d("it  = ${it.size}")
                rememberList.clear()
                rememberList.addAll(it)
            }
            NavHost(navController = navController, startDestination = "Home") {
                composable("Home") {
                    HomeScreen(rememberList, scanClick = {
                        featureCheckBlePermission(permissionState) {
                            scanViewModel.startScanner()
                        }
                    }, itemClick = {
                        scanViewModel.stopScanner()
                        navController.navigate("Detail")
                        selectDevice.value = it
                    })
                }
                composable("Detail") {
                    DetailScreen(backClick = {
                        navController.popBackStack()
                    })
                }
            }

        }
    }
}