package org.eson.liteble.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shon.ble.util.BleLog
import com.shon.ble.util.ByteUtil
import no.nordicsemi.android.support.v18.scanner.ScanResult

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
    scannerList: MutableList<ScanResult>,
    scanClick: () -> Unit,
    itemClick: (scanResult: ScanResult) -> Unit
) {

    Scaffold(
        topBar = {
            HomeTopBar(scanClick)
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


@Preview
@Composable
fun PreHomeTopBar() {
    HomeTopBar {}
}