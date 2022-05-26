package org.eson.liteble.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemScanResult(address: String, rssi: Int, name: String?, value: String?, onClick: () -> Unit) {
    Card(
        Modifier
            .clickable {
                onClick.invoke()
            }, elevation = 2.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 10.dp)
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                DeviceTopInfo(name = name, address = address)
                value?.let {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = it, style = TextStyle(
                            color = Color(0xff999999),
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Text(
                text = "rssi:$rssi",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = TextStyle(
                    color = Color(0xff999999),
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun DeviceTopInfo(name: String?, address: String) {
    Column(Modifier.wrapContentWidth()) {
        Text(
            text = name ?: "UnKnow",
            style = TextStyle(
                color = Color(0xff555555),
                fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = address,
            style = TextStyle(
                color = Color(0xff666666),
                fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
        )
    }
}


@Preview
@Composable
fun PreviewDeviceItem() {
    Box(
        Modifier
            .height(400.dp)
            .background(color = Color.White)
            .padding(10.dp, 10.dp)
    ) {
        ItemScanResult(
            name = "BLE_E1",
            address = "11:22:33:44:55:66",
            value = "[00 00]",
            rssi = -50
        ) {

        }
    }
}