package org.eson.liteble.main.composable

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shon.bluetooth.util.BleLog
import no.nordicsemi.android.support.v18.scanner.ScanResult

@Composable
fun HomeScreen(navController: NavHostController?,
               scannerList:MutableList<ScanResult>,
               scanClick: () -> Unit) {

    Scaffold(
        topBar = {
            HomeTopBar(scanClick) {

            }
        }
    ) {

        BleLog.d("scanner List = $scannerList")
        LazyColumn {
            items(scannerList) { scanItem ->
                DeviceItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Log.d("HomeScreen", " click navController = $navController")

                            navController?.navigate("Detail")
                        }, scanItem
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(scanClick: () -> Unit, settingClick: () -> Unit) {
    TopAppBar(title = {
        Text(text = "Lite Ble", color = Color.White)
    }, actions = {
        Button(onClick = {
            BleLog.d("Scan btn clicked")
            scanClick.invoke()

        }) {
            Text(text = "Scan", color = Color.White)
        }
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "设置",
            tint = Color.White,
            modifier = Modifier.wrapContentSize().clickable {
                settingClick.invoke()
            }
        )
    })
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(modifier: Modifier, scanResult: ScanResult) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .background(Color.White)
    ) {
        Column(Modifier.weight(weight = 1f)) {
            Text(
                text = scanResult.device.name ?: "未命名",
                style = TextStyle(
                    color = Color(0xbb000000),
                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = scanResult.device.address,
                style = TextStyle(
                    color = Color(0x99333333)
                )
            )
        }
        Icon(
            imageVector = Icons.Filled.Bluetooth,
            contentDescription = "蓝牙",
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
        )
    }
}


@Preview
@Composable
fun PreHomeTopBar() {
    HomeTopBar({}, {})
}