package org.eson.liteble.composable.detail

import android.bluetooth.BluetoothGattCharacteristic
import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.eson.liteble.R
import org.eson.liteble.utils.BleUUIDUtil
import java.util.*

@Composable
fun ItemCharacteristics(
    characteristicsUUID: String,
    getCharacteristicsProperties: (characteristics: String) -> Int,
    onReadInfo: suspend (characteristics: String) -> String?,
    onWriteClick: (characteristics: String) -> Unit,
    onNotify: (characteristics: String) -> Unit
) {
    val resultInfo = remember { mutableStateOf<String?>(null) }
    Row {
        CharacteristicsInfo(
            characterUUIDString = characteristicsUUID,
            result = resultInfo.value,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .wrapContentHeight(),
        )
        CharacteristicsActions(
            properties = getCharacteristicsProperties(characteristicsUUID),
            readAction = {
                MainScope().launch {
                    val readInfoResult = onReadInfo.invoke(characteristicsUUID)
                    resultInfo.value = readInfoResult
                }
            },
            writeAction = {
                onWriteClick.invoke(characteristicsUUID)
            }, notifyAction = {
                onNotify.invoke(characteristicsUUID)
            }
        )
    }

}

@Composable
fun CharacteristicsInfo(modifier: Modifier, characterUUIDString: String, result: String? = null) {
    val characterUUID = UUID.fromString(characterUUIDString)
    val characterName = BleUUIDUtil.getCharacterNameByUUID(characterUUID)
    val hexString = BleUUIDUtil.getHexValue(characterUUID)
    Column(modifier = modifier) {
        Text(
            text = characterName,
            style = TextStyle(
                color = Color(0xff666666),
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.requiredHeight(2.dp))

        Text(
            text = "Hex : $hexString",
            style = TextStyle(
                color = Color(0xff777777),
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.requiredHeight(2.dp))
        Text(
            text = characterUUIDString,
            style = TextStyle(
                color = Color(0xff777777),
                fontSize = 11.sp,
                fontFamily = FontFamily.Serif,
            )
        )

        if (!TextUtils.isEmpty(result)) {
            Text(
                text = result!!,
                style = TextStyle(
                    color = Color(0xff999999),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.requiredHeight(2.dp))
        }
    }
}

@Composable
fun CharacteristicsActions(
    properties: Int,
    readAction: () -> Unit,
    writeAction: () -> Unit, notifyAction: () -> Unit
) {
    Row(Modifier.wrapContentWidth()) {

        if ((properties and BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
            IconButton(onClick = {
                MainScope().launch {
                    readAction.invoke()
                }
            }, modifier = Modifier.wrapContentWidth()) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_operation_read_normal),
                    contentDescription = "read"
                )
            }
        }
        if ((properties and BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
            IconButton(onClick = { writeAction.invoke() }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_operation_send_normal),
                    contentDescription = "write"
                )
            }
        }
        if ((properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
            IconButton(onClick = { notifyAction.invoke() }) {
                Icon(
                    painter = painterResource(id = R.mipmap.ic_operation_start_notifications_normal),
                    contentDescription = "NOTIFY"
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCharacteristicsInfo() {
    Box(modifier = Modifier
        .background(Color.White)
        .padding(16.dp, 16.dp)) {
        CharacteristicsInfo(
            modifier = Modifier.fillMaxWidth(),
            characterUUIDString = "00002a27-0000-1000-8000-00805f9b34fb",
            result = "aaaa"
        )
    }
}


@Preview
@Composable
fun PreViewCharacteristicsActions() {
    CharacteristicsActions(
        properties = 2,
        readAction = { },
        writeAction = { }) {
    }
}