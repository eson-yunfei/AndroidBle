package org.eson.liteble.composable.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shon.ble.util.BleLog
import com.shon.ble.util.ByteUtil
import no.nordicsemi.android.support.v18.scanner.ScanResult

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
    scanning: Boolean,
    scannerList: MutableList<ScanResult>,
    scanClick: (Boolean) -> Unit,
    itemClick: (scanResult: ScanResult) -> Unit
) {

    Scaffold(
        topBar = {
            HomeTopBar(scanning, scanClick)
        }
    ) {

        Column {

            ScanSettingLayout()
            LazyColumn(
                contentPadding = PaddingValues(16.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp), //item 间距
            ) {
                items(scannerList) { scanItem ->
                    val device = scanItem.device
                    val data =
                        scanItem.scanRecord?.getManufacturerSpecificData(1)

                    val value = when (data) {
                        null -> null
                        else -> {
                            ByteUtil.getHexString(data)
                        }
                    }
                    ItemScanResult(
                        address = device.address,
                        rssi = scanItem.rssi,
                        name = device.name,
                        value = value
                    ) {
                        itemClick.invoke(scanItem)
                    }

                }
            }
        }

    }
}

@Composable
fun HomeTopBar(scanning: Boolean, scanClick: (Boolean) -> Unit) {
    TopAppBar(title = {
        Text(text = "Lite Ble", color = Color.White)
    }, actions = {
        if (scanning) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp),
                strokeWidth = 2.dp
            )
        }
        TextButton(onClick = {
            BleLog.d("Scan btn clicked")
            scanClick.invoke(!scanning)
        }) {
            Text(text = if (scanning) "Stop Scan" else "Start Scan", color = Color.White)
        }
    })
}


@Preview
@Composable
fun PreHomeTopBar() {
    HomeTopBar(false) {}
}