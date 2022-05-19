package org.eson.liteble.main.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import com.shon.bluetooth.util.BleLog
import no.nordicsemi.android.support.v18.scanner.ScanResult
import org.eson.liteble.main.filterNoName
import org.eson.liteble.main.sortByRssi

@Composable
fun HomeScreen(
    scannerList: MutableList<ScanResult>,
    scanClick: () -> Unit,
    itemClick:(scanResult: ScanResult)->Unit
) {

    Scaffold(
        topBar = {
            HomeTopBar(scanClick)
        }
    ) {

        Column {

            SettingLayout()
            LazyColumn {
                items(scannerList) { scanItem ->
                    DeviceItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                itemClick.invoke(scanItem)
                            }, scanItem
                    )
                }
            }
        }

    }
}

@Composable
fun HomeTopBar(scanClick: () -> Unit) {
    TopAppBar(title = {
        Text(text = "Lite Ble", color = Color.White)
    }, actions = {
        Button(onClick = {
            BleLog.d("Scan btn clicked")
            scanClick.invoke()

        }) {
            Text(text = "Scan", color = Color.White)
        }
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
        Text(text = "Rssi:${scanResult.rssi}")
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingLayout() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Row() {
            Row(
                Modifier
                    .wrapContentSize()
                    .wrapContentHeight()) {
                Checkbox(checked = filterNoName.value, onCheckedChange = {
                    filterNoName.value = it
                })
                Text(text = "不显示无名称设备", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Row(
                Modifier
                    .wrapContentSize()
                    .wrapContentHeight()) {
                Checkbox(checked = sortByRssi.value, onCheckedChange = {
                    sortByRssi.value = it
                })
                Text(text = "RSSI 排序", modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
    }
}

@Composable
@Preview
fun PreSettingLayout() {

    SettingLayout()
}

@Preview
@Composable
fun PreHomeTopBar() {
    HomeTopBar {}
}