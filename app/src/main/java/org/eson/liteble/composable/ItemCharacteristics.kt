package org.eson.liteble.composable

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.text.TextUtils
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.eson.liteble.R
import org.eson.liteble.ext.ActionExt
import org.eson.liteble.utils.BleUUIDUtil
import java.util.*

@Composable
fun ItemCharacteristics(
    gatt: BluetoothGatt,
    gattService: BluetoothGattService,
    characteristics: BluetoothGattCharacteristic,
    onWriteClick: () -> Unit,
    onNotify: () -> Unit
) {
    val resultInfo = remember {
        mutableStateOf<String?>(null)
    }
    Row {
        CharacteristicsInfo(
            characterUUID = characteristics.uuid,
            result = resultInfo.value,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .wrapContentHeight(),

            )
        CharacteristicsActions(
            properties = characteristics.properties,
            readAction = {
                MainScope().launch {
                    val readInfoResult =
                        ActionExt.readInfo(
                            gatt,
                            gattService.uuid.toString(),
                            characteristics.uuid.toString()
                        )
                    resultInfo.value = readInfoResult
                }
            },
            writeAction = onWriteClick, notifyAction = onNotify
        )
    }

}

@Composable
fun CharacteristicsInfo(modifier: Modifier, characterUUID: UUID, result: String? = null) {
    val characterName = BleUUIDUtil.getCharacterNameByUUID(characterUUID)
    val hexString = BleUUIDUtil.getHexValue(characterUUID)
    Column(modifier = modifier) {
        Text(
            text = characterName,
            style = TextStyle(
                color = Color(0xff666666),
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
            )
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        Text(
            text = "Hex : $hexString",
            style = TextStyle(
                color = Color(0xff666666),
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic
            )
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
        )
        Text(
            text = "Full: $characterUUID",
            style = TextStyle(
                color = Color(0xff666666),
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
            )
        )

        if (!TextUtils.isEmpty(result)) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            )
            Text(
                text = result!!,
                style = TextStyle(
                    color = Color(0xff666666),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic
                )
            )
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
    CharacteristicsInfo(
        modifier = Modifier.fillMaxWidth(),
        characterUUID = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb"),
        result = "aaaa"
    )
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