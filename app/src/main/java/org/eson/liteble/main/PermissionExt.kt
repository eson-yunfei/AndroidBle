package org.eson.liteble.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.shon.bluetooth.util.BleLog

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
//
//@OptIn(ExperimentalPermissionsApi::class)
//private fun getTextToShowGivenPermissions(
//    permissions: List<PermissionState>,
//    shouldShowRationale: Boolean
//): String {
//    val revokedPermissionsSize = permissions.size
//    if (revokedPermissionsSize == 0) return ""
//
//    val textToShow = StringBuilder().apply {
//        append("The ")
//    }
//
//    for (i in permissions.indices) {
//        textToShow.append(permissions[i].permission)
//        when {
//            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
//                textToShow.append(", and ")
//            }
//            i == revokedPermissionsSize - 1 -> {
//                textToShow.append(" ")
//            }
//            else -> {
//                textToShow.append(", ")
//            }
//        }
//    }
//    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
//    textToShow.append(
//        if (shouldShowRationale) {
//            " important. Please grant all of them for the app to function properly."
//        } else {
//            " denied. The app cannot function without them."
//        }
//    )
//    return textToShow.toString()
//}