package org.eson.liteble.ext

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.shon.ble.util.BleLog

fun getPermissionList(): List<String> {
    val permissionList = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        permissionList.add(Manifest.permission.BLUETOOTH_SCAN)
        permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
    }
    return permissionList
}

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
fun featureCheckBlePermission(permissionsState: MultiplePermissionsState, onPermissionGranted:()->Unit) {
   if ( permissionsState.allPermissionsGranted){
       BleLog.d(" all Permissions Granted ")
       onPermissionGranted.invoke()
   }else{
       BleLog.d("request permission")
       permissionsState.launchMultiplePermissionRequest()
   }
}
