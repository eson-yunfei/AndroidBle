package org.eson.liteble.composable.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.eson.liteble.utils.BleUUIDUtil
import java.util.*


@Composable
fun GattServiceTopLayout(serviceUUIDString: String) {
    val serviceUUid = UUID.fromString(serviceUUIDString)
    val serviceName = BleUUIDUtil.getServiceNameByUUID(serviceUUid)
    val shortUUID = BleUUIDUtil.getHexValue(serviceUUid)
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = serviceName, style = TextStyle(
                color = Color(0xff555555),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.requiredHeight(5.dp))
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = shortUUID,
                style = TextStyle(
                    color = Color(0xff777777),
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif
                )
            )
            Spacer(modifier = Modifier.requiredWidth(5.dp))
            Text(
                text = "($serviceUUIDString)",
                style = TextStyle(
                    color = Color(0xff999999),
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic
                ), modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }


}

@Composable
fun ItemGattService(
    serviceUUIDString: String,
    characteristicsUUIDStrings: MutableList<String>,
    getCharacteristicsProperties: (characteristics: String) -> Int,
    onReadData: suspend (characteristics: String) -> String?,
    onWriteClick: (characteristics: String) -> Unit,
    onNotify: (characteristics: String) -> Unit
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            GattServiceTopLayout(serviceUUIDString)
            if (characteristicsUUIDStrings.isNotEmpty()) {
                characteristicsUUIDStrings.forEach {
                    Spacer(modifier = Modifier.requiredHeight(10.dp))
                    ItemCharacteristics(
                        characteristicsUUID = it,
                        getCharacteristicsProperties,
                        onReadData,
                        onWriteClick,
                        onNotify
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewGattServiceItem() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp, 16.dp)
    ) {
        ItemGattService(
            serviceUUIDString = "0000180a-0000-1000-8000-00805f9b34fb",
            characteristicsUUIDStrings = arrayListOf("00002a27-0000-1000-8000-00805f9b34fb"),
            getCharacteristicsProperties = { return@ItemGattService 2 },
            onReadData = { return@ItemGattService "" },
            onWriteClick = {},
            onNotify = {}
        )
    }
}

